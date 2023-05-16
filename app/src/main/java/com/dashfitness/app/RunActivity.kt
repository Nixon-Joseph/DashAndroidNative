package com.dashfitness.app

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.dashfitness.app.database.RunData
import com.dashfitness.app.databinding.ActivityRunBinding
import com.dashfitness.app.database.RunDatabaseDao
import com.dashfitness.app.database.RunLocationData
import com.dashfitness.app.database.RunSegmentData
import com.dashfitness.app.services.LatLngAltTime
import com.dashfitness.app.services.Polyline
import com.dashfitness.app.services.Polylines
import com.dashfitness.app.services.TrackingService
import com.dashfitness.app.ui.main.run.models.RunActivityModel
import com.dashfitness.app.ui.main.run.models.RunSegment
import com.dashfitness.app.ui.run.RunMapFragment
import com.dashfitness.app.ui.run.RunStatsFragment
import com.dashfitness.app.ui.viewmodels.RunViewModel
import com.dashfitness.app.util.Constants.ACTION_PAUSE_SERVICE
import com.dashfitness.app.util.Constants.ACTION_START_OR_RESUME_SERVICE
import com.dashfitness.app.util.Constants.ACTION_STOP_SERVICE
import com.dashfitness.app.util.calculatePace
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@OptIn(DelicateCoroutinesApi::class)
@AndroidEntryPoint
class RunActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    @Inject
    lateinit var dataSource: RunDatabaseDao
    private lateinit var binding: ActivityRunBinding
    private lateinit var viewModel: RunViewModel
    private lateinit var runMapFragment: RunMapFragment
    private lateinit var runStatsFragment: RunStatsFragment
    private lateinit var tts: TextToSpeech
    private lateinit var segments: ArrayList<RunSegment>
    private lateinit var runActivityModel: RunActivityModel
    private var isTreadmill: Boolean = false
    private val trackedRunSegments = ArrayList<TrackedRunSegment>()
    private var bundle: Bundle? = null
    private var isTracking = false
    private var pathPoints: Polylines = ArrayList()
    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bundle = savedInstanceState
        binding = DataBindingUtil.setContentView(this, R.layout.activity_run)

        binding.lifecycleOwner = this
        val toolbar = Toolbar(this)
        setSupportActionBar(toolbar)

        viewModel = ViewModelProvider(this)[RunViewModel::class.java]
        binding.viewModel = viewModel

        runMapFragment = RunMapFragment(viewModel)
        runStatsFragment = RunStatsFragment(viewModel)

        val adapter = ViewPageAdapter(supportFragmentManager)
        adapter.addFragment(runMapFragment, "Map")
        adapter.addFragment(runStatsFragment, "Stats")

        binding.viewPager.adapter = adapter
        binding.tabs.setupWithViewPager(binding.viewPager)

        tts = TextToSpeech(this, this)

        runActivityModel = intent.getSerializableExtra("runActivityInfo") as RunActivityModel
        segments = runActivityModel.segments
        isTreadmill = intent.getBooleanExtra("isTreadmill", false)

        preferences = PreferenceManager.getDefaultSharedPreferences(this)

        TrackingService.reset()

        viewModel.setupRun(preferences)

        subscribeToObservers()

        addViewModelEvents()
    }

    override fun onInit(status: Int) { }

    private fun addViewModelEvents() {
        viewModel.endRun += {
            sendCommandToService(ACTION_PAUSE_SERVICE)
            val builder = MaterialAlertDialogBuilder(this)
            builder.setTitle("Are you sure?")
            builder.setMessage("You are about to end your run.\n\nAre you sure you want to proceed?")
            builder.setPositiveButton("End Run") { _, _ ->
                GlobalScope.launch {
                    stopRun()
                    sendCommandToService(ACTION_STOP_SERVICE)
                }
            }
            builder.setNegativeButton(android.R.string.cancel) { _, _ ->
                sendCommandToService(ACTION_START_OR_RESUME_SERVICE)
            }
            builder.show()
        }

        viewModel.cancelRun += {
            setResult(RESULT_CANCELED)
            finish()
        }
        viewModel.startRun += {
            TrackingService.setupRun(segments, tts, isTreadmill, preferences)
            sendCommandToService(ACTION_START_OR_RESUME_SERVICE)
            viewModel.runState.postValue(RunViewModel.RunStates.Running)
            tts.speak("Lets go!", TextToSpeech.QUEUE_ADD, bundle, UUID.randomUUID().toString())
        }
        viewModel.resumeRun += {
            sendCommandToService(ACTION_START_OR_RESUME_SERVICE)
            viewModel.runState.postValue(RunViewModel.RunStates.Running)
        }
        viewModel.pauseRun += {
            sendCommandToService(ACTION_PAUSE_SERVICE)
            viewModel.runState.postValue(RunViewModel.RunStates.Paused)
        }
    }

    private fun subscribeToObservers() {
        TrackingService.isTracking.observe(this) {
            updateTracking(it)
        }

        TrackingService.timeRunInMillis.observe(this) {
            viewModel.updateTimeElapsed(it)
        }

        TrackingService.totalDistance.observe(this) {
            viewModel.updateTotalDistance(it)
        }

        TrackingService.totalElevationChange.observe(this) {
            viewModel.updateElevationChange(it)
        }

        TrackingService.pathPoints.observe(this) {
            // for some reason it sends an empty result at the end, this saves us from that
            if (it.isNotEmpty()) {
                pathPoints = it.toMutableList()
            }
        }

        TrackingService.newSegment.observe(this) { it ->
            if (it == null) {
                GlobalScope.launch {
                    stopRun()
                }
            } else {
                val now = System.currentTimeMillis()
                finishCurrentSegment(now)
                trackedRunSegments.add(TrackedRunSegment(now, it))
            }
        }
    }

    private fun finishCurrentSegment(time: Long) {
        if (trackedRunSegments.isNotEmpty()) {
            trackedRunSegments.last().apply {
                stopTime = time
                distance = TrackingService.totalSegmentDistance
            }
        }
    }

    private fun updateTracking(isTracking: Boolean) {
        this.isTracking = isTracking
    }

    private suspend fun stopRun() {
        sendCommandToService(ACTION_STOP_SERVICE)
        val endTime = System.currentTimeMillis()
        finishCurrentSegment(endTime)
        val started = TrackingService.timeStarted
        val distance = TrackingService.totalDistance.value!!
        val timeRun = TrackingService.timeRunInMillis.value!!
        val caloriesBurnt = viewModel.estimateCaloriesBurned(distance, timeRun)
        val runId = coroutineScope { withContext(Dispatchers.IO) { saveRun(endTime, started, distance, timeRun, caloriesBurnt) } }
        val intent = Intent()
        intent.putExtra("RunId", runId)
        setResult(RESULT_OK, intent)
        finish()
    }

    private fun sendCommandToService(action: String) =
        Intent(this, TrackingService::class.java).also {
            it.action = action
            this.startService(it)
        }

    private suspend fun saveRun(endTime: Long, timeStarted: Long, totalDistance: Double, timeRun: Long, caloriesBurnt: Int): Long {
        val runId = dataSource.insert(
            RunData(
                startTimeMilli = timeStarted,
                endTimeMilli = endTime,
                totalDistance = totalDistance,
                averagePace = calculatePace(timeRun, totalDistance),
                calories = caloriesBurnt,
                title = "Run",
                planRunCode = runActivityModel.planRunCode,
                planRunFinished = TrackingService.allSegmentsFinished
            )
        )
        trackedRunSegments.forEach { trackedRunSegment ->
            dataSource.insert(
                RunSegmentData(
                    runId = runId,
                    startTimeMilli = trackedRunSegment.startTime,
                    endTimeMilli = trackedRunSegment.stopTime,
                    runSpeed = trackedRunSegment.speed,
                    distance = trackedRunSegment.distance,
                    pace = calculatePace(trackedRunSegment.stopTime - trackedRunSegment.startTime, trackedRunSegment.distance)
                )
            )
        }
        val locDataList = ArrayList<RunLocationData>()
        listOf(pathPoints.forEachIndexed { lineIndex, line: Polyline ->
            listOf(line.forEachIndexed { locIndex, loc: LatLngAltTime ->
                locDataList.add(
                    RunLocationData(
                        runId = runId,
                        latitude = loc.latitude,
                        longitude = loc.longitude,
                        altitude = loc.altitude,
                        polylineIndex = lineIndex,
                        time = loc.time,
                        index = locIndex
                    )
                )
            })
        })
        dataSource.insert(locDataList)
        return runId
    }

    class ViewPageAdapter(supportFragmentManager: FragmentManager) : FragmentStatePagerAdapter(supportFragmentManager) {
        private val _fragmentList = ArrayList<Fragment>();
        private val _fragmentTitleList = ArrayList<String>();

        override fun getItem(position: Int): Fragment {
            return _fragmentList[position];
        }

        override fun getCount(): Int {
            return _fragmentList.size;
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return _fragmentTitleList[position];
        }

        fun addFragment(fragment: Fragment, title: String) {
            _fragmentList.add(fragment);
            _fragmentTitleList.add(title);
        }
    }

    private class TrackedRunSegment(var startTime: Long, segment: RunSegment) : RunSegment(segment.type, segment.speed, segment.value) {
        var distance: Double = 0.0
        var stopTime: Long = 0L
    }
}

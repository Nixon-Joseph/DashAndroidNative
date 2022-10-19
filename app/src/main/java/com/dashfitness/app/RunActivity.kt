package com.dashfitness.app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.ViewModelProvider
import com.dashfitness.app.database.RunData
import com.dashfitness.app.databinding.ActivityRunBinding
import com.dashfitness.app.database.RunDatabaseDao
import com.dashfitness.app.database.RunLocationData
import com.dashfitness.app.database.RunSegmentData
import com.dashfitness.app.services.TrackingService
import com.dashfitness.app.ui.main.run.models.RunSegment
import com.dashfitness.app.ui.main.run.models.RunSegmentSpeed
import com.dashfitness.app.ui.run.RunMapFragment
import com.dashfitness.app.ui.run.RunStatsFragment
import com.dashfitness.app.util.Constants.ACTION_PAUSE_SERVICE
import com.dashfitness.app.util.Constants.ACTION_START_OR_RESUME_SERVICE
import com.dashfitness.app.util.Constants.ACTION_STOP_SERVICE
import com.dashfitness.app.util.calculatePace
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@OptIn(DelicateCoroutinesApi::class)
@AndroidEntryPoint
class RunActivity : AppCompatActivity() {
    @Inject
    lateinit var dataSource: RunDatabaseDao
    private lateinit var binding: ActivityRunBinding
    private lateinit var viewModel: RunViewModel
    private lateinit var runMapFragment: RunMapFragment
    private lateinit var runStatsFragment: RunStatsFragment
    private lateinit var tts: TextToSpeech
    private lateinit var segments: ArrayList<RunSegment>
    private var bundle: Bundle? = null
    private var isTracking = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bundle = savedInstanceState
        binding = DataBindingUtil.setContentView(this, R.layout.activity_run)

        binding.lifecycleOwner = this
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

        tts = TextToSpeech(this) { }

        TrackingService.tts = tts

        segments = intent.getSerializableExtra("segments") as ArrayList<RunSegment>

        subscribeToObservers()

        addViewModelEvents()
    }

    private fun addViewModelEvents() {
        viewModel.endRun += {
            val builder = MaterialAlertDialogBuilder(this)
            builder.setTitle("Are you sure?")
            builder.setMessage("You are about to end your run.\n\nAre you sure you want to proceed?")
            builder.setPositiveButton("End Run") { _, _ ->
                GlobalScope.async {
                    stopRun()
                }
            }
            builder.setNegativeButton(android.R.string.no) { _, _ -> }
            builder.show()
        }

        viewModel.cancelRun += { finish() }
        viewModel.startRun += {
            TrackingService.runSegments = segments
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

        TrackingService.newSegment.observe(this) {

        }
    }

    private fun updateTracking(isTracking: Boolean) {
        this.isTracking = isTracking
    }

    private suspend fun stopRun() {
        sendCommandToService(ACTION_STOP_SERVICE)
        val endTime = System.currentTimeMillis()
        saveRun(endTime)
        finish()
    }

    private fun sendCommandToService(action: String) =
        Intent(this, TrackingService::class.java).also {
            it.action = action
            this.startService(it)
        }

    private suspend fun saveRun(endTime: Long) {
        coroutineScope {
            withContext(Dispatchers.IO) {
                val runId = dataSource.insert(
                    RunData(
                        startTimeMilli = TrackingService.timeStarted,
                        endTimeMilli = endTime,
                        totalDistance = TrackingService.totalDistance.value!!,
                        averagePace = calculatePace(TrackingService.timeRun, TrackingService.totalDistance.value!!)
                    )
                )
                val segmentId = dataSource.insert(
                    RunSegmentData(
                        runId = runId,
                        startTimeMilli = TrackingService.timeStarted,
                        endTimeMilli = endTime
                    )
                )
                val locDataList = ArrayList<RunLocationData>()
                listOf(TrackingService.runLocations.forEachIndexed { index, loc ->
                    locDataList.add(
                        RunLocationData(
                            segmentId = segmentId,
                            runId = runId,
                            latitude = loc.latitude,
                            longitude = loc.longitude,
                            altitude = loc.altitude,
                            index = index
                        )
                    )
                })
                dataSource.insert(locDataList)
            }
            finish()
        }
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
}

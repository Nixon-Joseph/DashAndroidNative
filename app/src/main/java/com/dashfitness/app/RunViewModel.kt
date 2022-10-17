package com.dashfitness.app

import android.content.BroadcastReceiver
import android.location.Location
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.dashfitness.app.database.RunData
import com.dashfitness.app.database.RunDatabaseDao
import com.dashfitness.app.database.RunLocationData
import com.dashfitness.app.database.RunSegmentData
import com.dashfitness.app.ui.main.run.models.RunSegment
import com.dashfitness.app.ui.main.run.models.RunSegmentSpeed
import com.dashfitness.app.ui.main.run.models.RunSegmentType
import com.dashfitness.app.util.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import java.lang.ref.WeakReference
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.concurrent.schedule
import kotlin.math.max
import kotlin.math.min

@HiltViewModel
class RunViewModel @Inject constructor(private val database: RunDatabaseDao) : ViewModel() {
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private var currentSegmentIndex = -1
    private val currentSegment: RunSegment?
        get() = if (currentSegmentIndex >= 0) { _segments[currentSegmentIndex] } else { null }
    private var hasSegments: Boolean = false

    private val _latLngs = MutableLiveData<ArrayList<LatLng>>()
    val latLngs: LiveData<ArrayList<LatLng>>
        get() = _latLngs
    private val _routeBounds = MutableLiveData<LatLngBounds>()
    val routeBounds: LiveData<LatLngBounds>
        get() = _routeBounds
    private val _timeElapsedString = MutableLiveData<String>()
    val timeElapsedString: LiveData<String>
        get() = _timeElapsedString
    private val _totalDistanceString = MutableLiveData<String>()
    val totalDistanceString: LiveData<String>
        get() = _totalDistanceString
    private val _averagePaceString = MutableLiveData<String>()
    val averagePaceString: LiveData<String>
        get() = _averagePaceString
    private val _caloriesBurntString = MutableLiveData<String>()
    val caloriesBurntString: LiveData<String>
        get() = _caloriesBurntString
    private val _elevationChangeString = MutableLiveData<String>()
    val elevationChangeString: LiveData<String>
        get() = _elevationChangeString
    private val _elevationTypeString = MutableLiveData<String>()
    val elevationTypeString: LiveData<String>
        get() = _elevationTypeString
    private val _distanceTypeString = MutableLiveData<String>()
    val distanceTypeString: LiveData<String>
        get() = _distanceTypeString

    private val runState = MutableLiveData<RunStates>()

    private val onEndRun = EventHandler<Boolean>()
    val endRun = Event(onEndRun)
    private val onFinishActivity = EventHandler<Boolean>()
    val finishActivity = Event(onFinishActivity)

    private var _timeElapsed = 0L
    private var _timeElapsedInSegment = 0L
    private var totalSegmentDistance: Double = 0.0
    private var startTime: Long = 0L
    private var minAltitude: Double? = null
    private var maxAltitude: Double? = null
    var minLat: Double? = null
    var maxLat: Double? = null
    var minLng: Double? = null
    var maxLng: Double? = null
    private var totalDistance: Double = 0.0
    private var averagePace: Long = 0L
    private var caloriesBurnt: Int = 0
    private var elevationChange: Double = 0.0
    private var isMetric: Boolean = false

    private lateinit var _receiver: LocationBroadcastReceiver
    private lateinit var _locService: WeakReference<LocationService>

    val startRunVisibility = Transformations.map(runState) {
        when (it) {
            RunStates.Unstarted -> View.VISIBLE
            else -> View.GONE
        }
    }
    val restartRunVisibility = Transformations.map(runState) {
        when (it) {
            RunStates.Paused -> View.VISIBLE
            else -> View.GONE
        }
    }
    val cancelRunVisibility = Transformations.map(runState) {
        when (it) {
            RunStates.Unstarted -> View.VISIBLE
            else -> View.GONE
        }
    }
    val endRunVisibility = Transformations.map(runState) {
        when (it) {
            RunStates.Paused, RunStates.Finished -> View.VISIBLE
            else -> View.GONE
        }
    }
    val pauseRunVisibility = Transformations.map(runState) {
        when (it) {
            RunStates.Running -> View.VISIBLE
            else -> View.GONE
        }
    }

    private var timer: TimerTask? = null

    private lateinit var _startLocService: () -> Unit
    private lateinit var _stopLocService: () -> Unit
    private lateinit var _unregisterReceiver: (r: BroadcastReceiver) -> Unit
    private lateinit var _segments: ArrayList<RunSegment>
    private var _serviceRunning = false
    private lateinit var _tts: TextToSpeech
    private var _bundle: Bundle? = null

    init {
        _timeElapsedString.value = "00:00"
        _totalDistanceString.value = "0.0"
        _averagePaceString.value = "00:00"
        _caloriesBurntString.value = "0"
        _elevationChangeString.value = "0"
        _elevationTypeString.value = "ft"
        _distanceTypeString.value = "miles"
        runState.value = RunStates.Unstarted
    }

    fun initialize(
        registerReceiver: (r: BroadcastReceiver) -> Unit,
        unregisterReceiver: (r: BroadcastReceiver) -> Unit,
        startLocService: () -> Unit,
        stopLocService: () -> Unit,
        segments: ArrayList<RunSegment>,
        tts: TextToSpeech,
        activityBundle: Bundle?
    ) {
        _unregisterReceiver = unregisterReceiver
        _startLocService = startLocService
        _stopLocService = stopLocService
        _locService = WeakReference(LocationService())
        _receiver = LocationBroadcastReceiver()
        _latLngs.value = ArrayList()
        _segments = segments
        _receiver.locationReceived += { loc ->
            if (loc != null) {
                Log.i("locationReceived", "lat: ${loc.latitude}, lon: ${loc.longitude}, accuracy: ${loc.accuracy}")
                if (runState.value == RunStates.Running) {
                    processNewLoc(loc)
                    _latLngs.value?.add(LatLng(loc.latitude, loc.longitude))
                    _latLngs.notifyObservers()
                } else {
                    updateMapCamera(loc)
                }
            }
        }
        registerReceiver(_receiver)
        _startLocService()
        _serviceRunning = true
        _tts = tts
        _bundle = activityBundle
    }

    private val locations = arrayListOf<Location>()

    private fun processNewLoc(loc: Location) {
        if (locations.size > 0) {
            val newDistance = locations.last().distanceTo(loc) / if(isMetric) 1000.0 else 1609.344
            totalDistance += newDistance
            totalSegmentDistance += newDistance
            minAltitude = minAltitude?.let { min(it, loc.altitude) }
            maxAltitude = maxAltitude?.let { max(it, loc.altitude) }
            minLat = minLat?.let { min(it, loc.latitude) }
            maxLat = maxLat?.let { max(it, loc.latitude) }
            minLng = minLng?.let { min(it, loc.longitude) }
            maxLng = maxLng?.let { max(it, loc.longitude) }
        } else {
            minAltitude = loc.altitude
            maxAltitude = loc.altitude
            minLat = loc.latitude
            maxLat = loc.latitude
            minLng = loc.longitude
            maxLng = loc.longitude
        }
        if (hasSegments) {
            currentSegment?.let {
                if (it.type == RunSegmentType.DISTANCE && totalSegmentDistance >= it.value) {
                    nextSegment()
                }
            }
        }
        elevationChange = maxAltitude!! - minAltitude!!
        val builder = LatLngBounds.builder()
        builder.include(LatLng(minLat!!, minLng!!))
        builder.include(LatLng(maxLat!!, maxLng!!))
        _routeBounds.value = builder.build()
        averagePace = calculatePace(_timeElapsed, totalDistance)
        caloriesBurnt = ((_timeElapsed.toDouble() / MILLIS_IN_SECOND / SECONDS_IN_MINUTE) * (2.5 * 1.3)).toInt()
        updateDisplayLabels()
        locations.add(loc)
    }

    private fun updateMapCamera(loc: Location) {
        val builder = LatLngBounds.builder()
        builder.include(LatLng(loc.latitude, loc.longitude))
        _routeBounds.value = builder.build()
    }

    private fun updateDisplayLabels() {
        _totalDistanceString.value = String.format("%.1f", totalDistance)
        _averagePaceString.value = if (averagePace > 0) convertLongToTimeString(averagePace) else "∞"
        _caloriesBurntString.value = caloriesBurnt.toString()
        _elevationChangeString.value = if(minAltitude != null && maxAltitude != null) String.format("%.1f", elevationChange) else "0"
        _elevationTypeString.value = if(isMetric) "m" else "ft"
        _distanceTypeString.value = if(isMetric) "km" else "miles"
    }

    private var _lastTimerTime = 0L
    private fun startRunTimer() {
        stopRunTimer()
        _lastTimerTime = System.currentTimeMillis()
        timer = Timer("RunTimer", false).schedule(0, 100) {
            onTimerSchedule(System.currentTimeMillis() - _lastTimerTime)
            _lastTimerTime = System.currentTimeMillis()
        }
    }

    private fun onTimerSchedule(deltaTime: Long) {
        if (runState.value == RunStates.Running) {
            _timeElapsed += deltaTime
            _timeElapsedInSegment += deltaTime
            _timeElapsedString.postValue(convertLongToTimeString(_timeElapsed))
            if (hasSegments) {
                currentSegment?.let {
                    if (it.type == RunSegmentType.TIME) {
                        val totalSeconds = _timeElapsedInSegment / MILLIS_IN_SECOND
                        val totalMinutes = totalSeconds / SECONDS_IN_MINUTE
                        if (totalMinutes >= it.value) {
                            nextSegment()
                        }
                    }
                }
            }
        }
    }

    private fun nextSegment() {
        // TODO: next segment stuff
        if (_segments.count() > currentSegmentIndex + 1) {
            currentSegmentIndex++
            _timeElapsedInSegment = 0L
            totalSegmentDistance = 0.0
            speakSegment()
            // TODO: speak previous segment info
        } else { // end of run
            onEndRunClicked()
        }
    }

    private fun stopRunTimer() {
        timer?.cancel()
    }

    fun onEndRunClicked() {
        onEndRun.invoke(true)
    }

    fun doEndRun() {
        terminateLocationService()
        _unregisterReceiver(_receiver)
        val endTime = System.currentTimeMillis()
        uiScope.async {
            saveRun(endTime)
        }
    }

    private suspend fun saveRun(endTime: Long) {
        coroutineScope {
            withContext(Dispatchers.IO) {
                val runId = database.insert(
                    RunData(
                        startTimeMilli = startTime,
                        endTimeMilli = endTime,
                        totalDistance = totalDistance,
                        averagePace = averagePace
                    )
                )
                val segmentId = database.insert(
                    RunSegmentData(
                        runId = runId,
                        startTimeMilli = startTime,
                        endTimeMilli = endTime
                    )
                )
                val locDataList = ArrayList<RunLocationData>()
                listOf(locations.forEachIndexed { index, loc ->
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
                database.insert(locDataList)
            }
            onFinishActivity.invoke(true)
        }
    }

    fun terminateLocationService() {
        if (_serviceRunning) {
            _stopLocService()
            _serviceRunning = false
        }
    }

    fun onCancelClicked() {
        onFinishActivity.invoke(true)
    }

    fun onPauseClicked() {
        runState.value = RunStates.Paused
    }

    fun onRestartClicked() {
        runState.value = RunStates.Running
    }

    fun onStartClicked() {
        startTime = System.currentTimeMillis()
        runState.value = RunStates.Running
        _tts.speak("Let's go!", TextToSpeech.QUEUE_ADD, _bundle, UUID.randomUUID().toString())
        if (_segments.isNotEmpty()) {
            hasSegments = true
            currentSegmentIndex = 0
            speakSegment()
        }
        startRunTimer()
    }

    private fun speakSegment() {
        currentSegment?.let {
            when (it.speed) {
                RunSegmentSpeed.Run -> _tts.speak("Run!", TextToSpeech.QUEUE_ADD, _bundle, UUID.randomUUID().toString())
                RunSegmentSpeed.Walk -> _tts.speak("Walk!", TextToSpeech.QUEUE_ADD, _bundle, UUID.randomUUID().toString())
            }
        }
    }

    enum class RunStates {
        Unstarted,
        Running,
        Paused,
        Finished
    }
}
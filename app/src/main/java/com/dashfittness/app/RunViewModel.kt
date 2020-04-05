package com.dashfittness.app

import android.content.BroadcastReceiver
import android.location.Location
import android.os.SystemClock
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.dashfittness.app.database.RunData
import com.dashfittness.app.database.RunDatabaseDao
import com.dashfittness.app.database.RunLocationData
import com.dashfittness.app.database.RunSegmentData
import com.dashfittness.app.util.*
import kotlinx.coroutines.*
import java.util.*
import kotlin.concurrent.schedule
import kotlin.math.max
import kotlin.math.min

class RunViewModel(val database: RunDatabaseDao) : ViewModel() {
    private var viewModelJob = Job();
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob);

    private val _locationUpdate = MutableLiveData<Location>()
    val locationUpdate: LiveData<Location>
        get() = _locationUpdate
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
    var startTime: Long = 0L
    private var minAltitude: Double? = null
    private var maxAltitude: Double? = null
    private var totalDistance: Double = 0.0
    private var averagePace: Long = 0L
    private var caloriesBurnt: Int = 0
    private var elevationChange: Double = 0.0
    private var isMetric: Boolean = false;

    private lateinit var _receiver: LocationBroadcastReceiver;
    private lateinit var _locService: LocationService

    val startRunVisibility = Transformations.map(runState) {
        when (it) {
            RunStates.Paused, RunStates.Unstarted -> View.VISIBLE
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

    private var timer: TimerTask? = null;

    private lateinit var _startLocService: () -> Unit;
    private lateinit var _stopLocService: () -> Unit;
    private lateinit var _unregisterReceiver: (r: BroadcastReceiver) -> Unit;
    private var _serviceRunning = false

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
        stopLocService: () -> Unit
    ) {
        _unregisterReceiver = unregisterReceiver
        _startLocService = startLocService
        _stopLocService = stopLocService
        _locService = LocationService()
        _receiver = LocationBroadcastReceiver()
        _receiver.locationReceived += { loc ->
            if (loc != null) {
                processNewLoc(loc)
                _locationUpdate.value = loc
            }
        }
        registerReceiver(_receiver)
    }

    private val locations = arrayListOf<Location>()

    private fun processNewLoc(loc: Location) {
        if (locations.size > 0) {
            totalDistance += locations.last().distanceTo(loc) / if(isMetric) 1000.0 else 1609.344
            minAltitude = minAltitude?.let { min(it, loc.altitude) }
            maxAltitude = maxAltitude?.let { max(it, loc.altitude) }
        } else {
            minAltitude = loc.altitude
            maxAltitude = loc.altitude
        }
        if(minAltitude != null && maxAltitude != null) { elevationChange = maxAltitude!! - minAltitude!! }
        averagePace = calculatePace(_timeElapsed, totalDistance)
        //TODO('set calories burnt')
        updateDisplayLabels()
        locations.add(loc)
    }

    private fun updateDisplayLabels() {
        _totalDistanceString.value = String.format("%.1f", totalDistance)
        _averagePaceString.value = if (averagePace > 0) convertLongToTimeString(averagePace) else "∞"
        _caloriesBurntString.value = caloriesBurnt.toString()
        _elevationChangeString.value = if(minAltitude != null && maxAltitude != null) String.format("%.1f", elevationChange) else "0"
        _elevationTypeString.value = if(isMetric) "m" else "ft"
        _distanceTypeString.value = if(isMetric) "km" else "miles"
    }

    private fun startRunTimer() {
        stopRunTimer()
        timer = Timer("RunTimer", false).schedule(100, 100) {
            if (runState.value == RunStates.Running) {
                val newTime = System.currentTimeMillis() - startTime
                _timeElapsed = newTime
                _timeElapsedString.postValue(convertLongToTimeString(newTime))
            }
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
        _unregisterReceiver(_receiver);
        val endTime = System.currentTimeMillis()
        uiScope.async {
            saveRun(endTime)
        }
    }

    suspend fun saveRun(endTime: Long) {
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
            _serviceRunning = false;
        }
    }

    fun onCancelClicked() {
        onFinishActivity.invoke(true)
    }

    fun onPauseClicked() {
        runState.value = RunStates.Paused
        stopRunTimer()
    }

    fun onStartClicked() {
        startTime = System.currentTimeMillis()
        runState.value = RunStates.Running
        startRunTimer()
        _startLocService()
        _serviceRunning = true;
    }

    enum class RunStates {
        Unstarted,
        Running,
        Paused,
        Finished
    }
}
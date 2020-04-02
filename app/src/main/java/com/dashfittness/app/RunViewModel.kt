package com.dashfittness.app

import android.annotation.SuppressLint
import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.*
import android.view.View
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.dashfittness.app.util.LocationBroadcastReceiver
import com.dashfittness.app.util.LocationService
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import java.util.*
import kotlin.concurrent.schedule
import kotlin.math.max
import kotlin.math.min

class RunViewModel : ViewModel() {
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

    private val _cancelClicked = MutableLiveData<Boolean>()
    val cancelClicked: LiveData<Boolean>
        get() = _cancelClicked
    private val _endRun = MutableLiveData<Boolean>()
    val endRun: LiveData<Boolean>
        get() = _endRun

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
        afterInit: (r: BroadcastReceiver) -> Unit,
        startLocService: () -> Unit,
        stopLocService: () -> Unit
    ) {
        _startLocService = startLocService;
        _stopLocService = stopLocService;
        _locService = LocationService()
        _receiver = LocationBroadcastReceiver()
        _receiver.locationReceived += { loc ->
            if (loc != null) {
                processNewLoc(loc)
                _locationUpdate.value = loc
            }
        }
        afterInit(_receiver)
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
        updateDisplayLabels()
        locations.add(loc)
    }

    private fun updateDisplayLabels() {
        _totalDistanceString.value = String.format("%.1f", totalDistance)
        _averagePaceString.value = if (totalDistance > 0.0) calcTimeElapsedString(((((_timeElapsed.toDouble() / 1000.0) / 60.0) / totalDistance) * 60000.0).toLong()) else "∞"
        _caloriesBurntString.value = "0"
        _elevationChangeString.value = if(minAltitude != null && maxAltitude != null) String.format("%.1f", (maxAltitude!! - minAltitude!!)) else "0"
        _elevationTypeString.value = if(isMetric) "m" else "ft"
        _distanceTypeString.value = if(isMetric) "km" else "miles"
    }

    private fun startRunTimer() {
        stopRunTimer()
        timer = Timer("RunTimer", false).schedule(100, 100) {
            if (runState.value == RunStates.Running) {
                val newTime = SystemClock.elapsedRealtime() - startTime
                _timeElapsed = newTime
                _timeElapsedString.postValue(calcTimeElapsedString(newTime))
            }
        }
    }

    private fun stopRunTimer() {
        timer?.cancel()
    }

    private fun calcTimeElapsedString(elapsed: Long): String {
        val totalSeconds = elapsed / 1000L
        val totalMinutes = totalSeconds / 60L
        val totalHours = totalMinutes / 60L
        var str = "";
        if (totalHours > 0L) {
            str = totalHours.toString().padStart(2, '0')
        }
        return str + "${(totalMinutes % 60L).toString().padStart(2, '0')}:${(totalSeconds % 60L).toString().padStart(2, '0')}"
    }

    fun onEndRunClicked() {
        _endRun.value = true
    }

    fun doEndRun() {
        _stopLocService()
    }

    fun afterEndRunClicked() {
        _endRun.value = false
    }

    fun onCancelClicked() {
        _cancelClicked.value = true;
    }

    fun afterCancelClicked() {
        _cancelClicked.value = false;
    }

    fun onPauseClicked() {
        runState.value = RunStates.Paused
        stopRunTimer()
    }

    fun onStartClicked() {
        startTime = SystemClock.elapsedRealtime()
        runState.value = RunStates.Running
        startRunTimer()
        _startLocService()
    }

    enum class RunStates {
        Unstarted,
        Running,
        Paused,
        Finished
    }
}
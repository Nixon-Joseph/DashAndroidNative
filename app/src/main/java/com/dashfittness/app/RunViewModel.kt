package com.dashfittness.app

import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import android.os.SystemClock
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.dashfittness.app.util.RunClickInterface
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import java.time.Duration
import java.util.*
import kotlin.concurrent.schedule

class RunViewModel : ViewModel(), RunClickInterface {
    private val _stateUpdate = MutableLiveData<RunState>();
    val stateUpdate: LiveData<RunState>
        get() = _stateUpdate
    private val _locationUpdate = MutableLiveData<Location>()
    val locationUpdate: LiveData<Location>
        get() = _locationUpdate
    private var _timeElapsed = 0L
    var running: Boolean = false
    var startTime: Long = 0L
    private var totalDistance: Double = 0.0
    private var averagePace: Long = 0L
    private var caloriesBurnt: Int = 0
    private var elevationChange: Double = 0.0

    private var state = RunState();

    private var timer: TimerTask? = null;

    private fun startRunTimer() {
        stopRunTimer()
        timer = Timer("RunTimer", false).schedule(100, 100) {
            val newTime: Long
            if (running) {
                newTime = startTime - SystemClock.elapsedRealtime()
                _timeElapsed = newTime
                state.timeElapsedString = calcTimeElapsedString(newTime)
            }
            _stateUpdate.postValue(state);
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

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult?.let {
                if (locationResult.locations.size > 0) {
                    _locationUpdate.value = locationResult.locations.last()
                }
            }
        }
    }

    fun stopLocationUpdates(fusedLocationClient: FusedLocationProviderClient) {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    fun startLocationUpdates(activity: RunActivity, fusedLocationClient: FusedLocationProviderClient) {
        if (
            ContextCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(activity, android.Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(activity, arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                android.Manifest.permission.INTERNET
            ), PERMISSION_REQUEST);
            startLocationUpdates(activity, fusedLocationClient)
        } else {
            var request = LocationRequest();
            request.maxWaitTime = 5000;
            request.interval = 5000;
            fusedLocationClient.requestLocationUpdates(request, locationCallback, Looper.getMainLooper());
        }
    }

    override fun onEndRunClicked() {
        TODO("Not yet implemented")
    }

    override fun onCancelClicked() {
        TODO("Not yet implemented")
    }

    override fun onPauseClicked() {
        TODO("Not yet implemented")
    }

    override fun onStartClicked() {
        TODO("Not yet implemented")
    }

    class RunState() {
        var running: Boolean = false
        var timeElapsedString: String = "00:00"
        var totalDistanceString: String = "0.0"
        var averagePaceString: String = "00:00"
        var caloriesBurntString: String = "0"
        var elevationChangeString: String = "0"
        var elevationTypeString: String = "ft"
        var distanceTypeString: String = "miles"
    }
}
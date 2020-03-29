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
    private var _timeElapsed = 0L
    var running: Boolean = false
    var startTime: Long = 0L
    private var totalDistance: Double = 0.0
    private var averagePace: Long = 0L
    private var caloriesBurnt: Int = 0
    private var elevationChange: Double = 0.0

    private var timer: TimerTask? = null;

    init {
        _timeElapsedString.value = "00:00"
        _totalDistanceString.value = "0.0"
        _averagePaceString.value = "00:00"
        _caloriesBurntString.value = "0"
        _elevationChangeString.value = "0"
        _elevationTypeString.value = "ft"
        _distanceTypeString.value = "miles"
    }

    private fun startRunTimer() {
        stopRunTimer()
        timer = Timer("RunTimer", false).schedule(100, 100) {
            if (running) {
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
        stopRunTimer()
    }

    override fun onStartClicked() {
        startTime = SystemClock.elapsedRealtime()
        running = true
        startRunTimer()
    }
}
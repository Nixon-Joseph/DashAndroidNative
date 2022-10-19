package com.dashfitness.app.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent.*
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.dashfitness.app.R
import com.dashfitness.app.ui.main.run.models.RunSegment
import com.dashfitness.app.ui.main.run.models.RunSegmentType
import com.dashfitness.app.util.Constants.ACTION_PAUSE_SERVICE
import com.dashfitness.app.util.Constants.ACTION_START_OR_RESUME_SERVICE
import com.dashfitness.app.util.Constants.ACTION_STOP_SERVICE
import com.dashfitness.app.util.Constants.FASTEST_LOCATION_INTERVAL
import com.dashfitness.app.util.Constants.LOCATION_UPDATE_INTERVAL
import com.dashfitness.app.util.Constants.NOTIFICATION_CHANNEL_ID
import com.dashfitness.app.util.Constants.NOTIFICATION_CHANNEL_NAME
import com.dashfitness.app.util.Constants.NOTIFICATION_ID
import com.dashfitness.app.util.Constants.TIMER_UPDATE_INTERVAL
import com.dashfitness.app.util.MILLIS_IN_SECOND
import com.dashfitness.app.util.SECONDS_IN_MINUTE
import com.dashfitness.app.util.TrackingUtility
import com.dashfitness.app.util.calculateDistance
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.max
import kotlin.math.min

typealias Polyline = MutableList<LatLng>
typealias Polylines = MutableList<Polyline>

@AndroidEntryPoint
class TrackingService : LifecycleService() {
    var isFirstRun = true
    var serviceKilled = false

    @Inject
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private val timeRunInSeconds = MutableLiveData<Long>()

    @Inject
    lateinit var baseNotificationBuilder: NotificationCompat.Builder

    lateinit var curNotification: NotificationCompat.Builder

    companion object {
        var runSegments = ArrayList<RunSegment>()
        val timeRunInMillis = MutableLiveData<Long>()
        val isTracking = MutableLiveData<Boolean>()
        val pathPoints = MutableLiveData<Polylines>()
        val totalDistance = MutableLiveData<Double>()
        val totalElevationChange = MutableLiveData<Double>()
        var currentSegmentIndex = -1
        val currentSegment: RunSegment?
            get() = if (currentSegmentIndex >= 0) { runSegments[currentSegmentIndex] } else { null }
        private var hasSegments: Boolean = true
        var timeElapsedInSegment = 0L
        var totalSegmentDistance = 0.0
        val newSegment = MutableLiveData<RunSegment>()
        var isMetric: Boolean = false
        var timeStarted = 0L
        var timeRun = 0L
        val runLocations = ArrayList<Location>()
    }

    private fun postInitialValues() {
        isTracking.postValue(false)
        pathPoints.postValue(mutableListOf())
        timeRunInSeconds.postValue(0L)
        timeRunInMillis.postValue(0L)
        totalDistance.postValue(0.0)
        totalElevationChange.postValue(0.0)
    }

    override fun onCreate() {
        super.onCreate()
        curNotification = baseNotificationBuilder
        postInitialValues()

        isTracking.observe(this, Observer {
            updateLocationTracking(it)
            updateNotificationTrackingState(it)
        })
    }

    private fun killService() {
        serviceKilled = true
        isFirstRun = true
        pauseService()
        postInitialValues()
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action) {
                ACTION_START_OR_RESUME_SERVICE -> {
                    if (isFirstRun) {
                        hasSegments = runSegments.isNotEmpty()
                        startForegroundService()
                        currentSegmentIndex = -1
                        nextSegment()
                        isFirstRun = false
                    } else {
                        // TODO resume
                        startTimer()
                    }
                }
                ACTION_PAUSE_SERVICE -> {
                    pauseService()
                }
                ACTION_STOP_SERVICE -> {
                    killService()
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private var isTimerEnabled = false
    private var lapTime = 0L
    private var lastSecondTimestamp = 0L

    private fun startTimer() {
        addEmptyPolyline()
        isTracking.postValue(true)
        timeStarted = System.currentTimeMillis()
        isTimerEnabled = true
        CoroutineScope(Dispatchers.Main).launch {
            while(isTracking.value!!) {
                lapTime = System.currentTimeMillis() - timeStarted

                timeRunInMillis.postValue(timeRun + lapTime)
                if (timeRunInMillis.value!! >= lastSecondTimestamp + 1000L) {
                    timeRunInSeconds.postValue(timeRunInSeconds.value!! + 1)
                    lastSecondTimestamp += 1000L
                    if (hasSegments) {
                        currentSegment?.let {
                            if (it.type == RunSegmentType.TIME) {
                                val totalSeconds = (timeElapsedInSegment + lapTime) / MILLIS_IN_SECOND
                                val totalMinutes = totalSeconds / SECONDS_IN_MINUTE
                                Timber.i("Time in segment: ")
                                if (totalMinutes >= it.value) {
                                    nextSegment()
                                }
                            }
                        }
                    }
                }
                delay(TIMER_UPDATE_INTERVAL)
            }
            timeElapsedInSegment += lapTime
            timeRun += lapTime
        }
    }

    private fun updateNotificationTrackingState(isTracking: Boolean) {
        val notificationActionText = if (isTracking) "Pause" else "Resume"
        val pendingIntent = if (isTracking) {
            val pauseIntent = Intent(this, TrackingService::class.java).apply {
                action = ACTION_PAUSE_SERVICE
            }
            getService(this, 1, pauseIntent, FLAG_IMMUTABLE)
        } else {
            val resumeIntent = Intent(this, TrackingService::class.java).apply {
                action = ACTION_START_OR_RESUME_SERVICE
            }
            getService(this, 2, resumeIntent, FLAG_IMMUTABLE)
        }
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        curNotification.javaClass.getDeclaredField("mActions").apply {
            isAccessible = true
            set(curNotification, ArrayList<NotificationCompat.Action>())
        }
        if (!serviceKilled) {
            curNotification = baseNotificationBuilder
                .addAction(R.drawable.ic_baseline_directions_walk_24, notificationActionText, pendingIntent)
            notificationManager.notify(NOTIFICATION_ID, curNotification.build())
        }
    }

    private fun updateLocationTracking(isTracking: Boolean) {
        if(isTracking) {
            if (TrackingUtility.hasLocationPermissions(this)) {
                val requestBuilder = com.google.android.gms.location.LocationRequest.Builder(LOCATION_UPDATE_INTERVAL)
                requestBuilder.apply {
                    setIntervalMillis(FASTEST_LOCATION_INTERVAL)
                    setPriority(PRIORITY_HIGH_ACCURACY)
                }
                fusedLocationProviderClient.requestLocationUpdates(
                    requestBuilder.build(),
                    locationCallback,
                    Looper.getMainLooper()
                )
            }
        } else {
            fusedLocationProviderClient.removeLocationUpdates((locationCallback))
        }
    }

    var lastLocation: Location? = null
    var minElevation: Double = 0.0
    var maxElevation: Double = 0.0

    val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            super.onLocationResult(result)
            if (isTracking.value!!) {
                var newDistance = 0.0
                result.locations.let { locations ->
                   for (location in locations) {
                       runLocations.add(location)
                       if (lastLocation == null) {
                           minElevation = location.altitude
                           maxElevation = location.altitude
                       } else {
                           newDistance += lastLocation!!.distanceTo(location)
                           minElevation = min(minElevation, location.altitude)
                           maxElevation = max(minElevation, location.altitude)
                       }
                       addPathPoint(location)
                       Log.d("THING", "NEW LOOCATION: ${location.latitude}, ${location.longitude}")
                       lastLocation = location
                   }
                }
                totalSegmentDistance += calculateDistance(newDistance, isMetric)
                totalDistance.value?.apply {
                    totalDistance.postValue(this + newDistance)
                }
                totalElevationChange.value?.apply {
                    totalElevationChange.postValue(maxElevation - minElevation)
                }
                if (hasSegments) {
                    currentSegment?.let {
                        if (it.type == RunSegmentType.DISTANCE && totalSegmentDistance >= it.value) {
                            nextSegment()
                        }
                    }
                }
            }
        }
    }

    private fun addPathPoint(location: Location?) {
        location?.let {
            val pos = LatLng(location.latitude, location.longitude)
            pathPoints.value?.apply {
                last().add(pos)
                pathPoints.postValue(this)
            }
        }
    }

    private fun addEmptyPolyline() = pathPoints.value?.apply {
        add(mutableListOf())
        pathPoints.postValue(this)
    } ?: pathPoints.postValue(mutableListOf(mutableListOf()))

    private fun startForegroundService() {
        startTimer()
        isTracking.postValue(true)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        startForeground(NOTIFICATION_ID, baseNotificationBuilder.build())

        timeRunInSeconds.observe(this, Observer {
            if (!serviceKilled) {
                val notification = curNotification
                    .setContentText(TrackingUtility.getFormattedStopwatchTime(it * 1000L))
                notificationManager.notify(NOTIFICATION_ID, notification.build())
            }
        })
    }

    private fun pauseService() {
        isTracking.postValue(false)
        lastLocation = null
        isTimerEnabled = false
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)
    }

    private fun nextSegment() {
        // TODO: next segment stuff
        if (runSegments.count() > currentSegmentIndex + 1) {
            currentSegmentIndex++
            timeElapsedInSegment = 0L
            totalSegmentDistance = 0.0
            timeRun += lapTime
            timeStarted = System.currentTimeMillis()
            newSegment.postValue(currentSegment)
            // TODO: speak previous segment info
        } else if (!isFirstRun) {
            // end of run
        }
    }
}
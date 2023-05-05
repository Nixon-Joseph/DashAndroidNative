package com.dashfitness.app.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent.*
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.dashfitness.app.R
import com.dashfitness.app.database.RunLocationData
import com.dashfitness.app.ui.main.run.models.RunSegment
import com.dashfitness.app.ui.main.run.models.RunSegmentSpeed
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
import kotlinx.coroutines.plus
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.math.max
import kotlin.math.min

typealias Polyline = MutableList<LatLngAltTime>
typealias Polylines = MutableList<Polyline>

@AndroidEntryPoint
class TrackingService : LifecycleService() {
    var isFirstRun = true
    var serviceKilled = false

    @Inject
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

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
        var tts: TextToSpeech? = null
        val timeRunInSeconds = MutableLiveData<Long>()
        var canGiveHalfwayCue = false
        var halfwayViaTime = false
        var halfwayValue = 0.0
        var alreadyHalfway = false
        fun setupRun(segments: ArrayList<RunSegment>?, textToSpeech: TextToSpeech) {
            timeRun = 0L
            timeRunInSeconds.value = 0
            timeStarted = 0L
            timeRunInMillis.value = 0L
            timeElapsedInSegment = 0L
            totalSegmentDistance = 0.0
            totalDistance.value = 0.0
            tts = textToSpeech
            if (!segments.isNullOrEmpty()) {
                segments.let {
                    runSegments = segments
                }
            } else {
                runSegments.add(RunSegment(RunSegmentType.NONE, RunSegmentSpeed.NONE, Float.MAX_VALUE));
            }
            if (runSegments.all { x -> x.type === RunSegmentType.TIME }) {
                canGiveHalfwayCue = true
                halfwayViaTime = true
                halfwayValue = runSegments.sumOf { x -> x.value.toDouble() } / 2.0
            } else if (runSegments.all { x -> x.type === RunSegmentType.DISTANCE }) {
                canGiveHalfwayCue = true
                halfwayValue = runSegments.sumOf { x -> x.value.toDouble() } / 2.0
            }
        }
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
                    val totalTimeInMinutes = (timeRun.toDouble() + lapTime.toDouble()) / MILLIS_IN_SECOND.toDouble() / SECONDS_IN_MINUTE.toDouble()
                    checkHalfway(totalTimeInMinutes, true)
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
                    checkHalfway(this + newDistance, false)
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

    private fun checkHalfway(compareValue: Double, valueIsTime: Boolean) {
        if (!alreadyHalfway && canGiveHalfwayCue) {
            if (halfwayViaTime == valueIsTime) {
                var modifiedCompare = compareValue
                if (!halfwayViaTime) {
                    modifiedCompare = compareValue / 1609.344
                }
                if (halfwayValue <= modifiedCompare) {
                    alreadyHalfway = true
                    tts?.speak("Halfway There!", TextToSpeech.QUEUE_ADD, null, UUID.randomUUID().toString())
                }
            }
        }
    }

    private fun addPathPoint(location: Location?) {
        location?.let {
            val pos = LatLngAltTime(location)
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
        if (runSegments.count() > currentSegmentIndex + 1) {
            currentSegmentIndex++
            newSegment.postValue(currentSegment)
            speakSegment(currentSegment)
            timeElapsedInSegment = 0L
            totalSegmentDistance = 0.0
            timeRun += lapTime
            timeStarted = System.currentTimeMillis()
            // TODO: speak previous segment info
        } else {
            if (isFirstRun) {
                runSegments.add(RunSegment(RunSegmentType.NONE, RunSegmentSpeed.NONE, 0f))
                newSegment.postValue(currentSegment)
                currentSegmentIndex++
                timeElapsedInSegment = 0L
                totalSegmentDistance = 0.0
            } else { // end of run
                tts?.speak("You made it! Great job.", TextToSpeech.QUEUE_ADD, null, UUID.randomUUID().toString())
                newSegment.postValue(null)
            }
        }
    }

    private fun speakSegment(segment: RunSegment?) {
        segment?.let {
            when (it.speed) {
                RunSegmentSpeed.RUN -> tts?.speak("Run!", TextToSpeech.QUEUE_ADD, null, UUID.randomUUID().toString())
                RunSegmentSpeed.WALK -> tts?.speak("Brisk Walk!", TextToSpeech.QUEUE_ADD, null, UUID.randomUUID().toString())
                RunSegmentSpeed.WARM_UP -> tts?.speak("Warm Up!", TextToSpeech.QUEUE_ADD, null, UUID.randomUUID().toString())
                RunSegmentSpeed.COOL_DOWN -> tts?.speak("Cool Down!", TextToSpeech.QUEUE_ADD, null, UUID.randomUUID().toString())
                RunSegmentSpeed.TEMPO_RUN -> tts?.speak("Tempo Run!", TextToSpeech.QUEUE_ADD, null, UUID.randomUUID().toString())
                RunSegmentSpeed.NONE -> {}
            }
        }
    }
}

class LatLngAltTime {
    var latitude: Double = 0.0
    var longitude: Double = 0.0
    var altitude: Double = 0.0
    var time: Long = 0L

    constructor(latitude: Double, longitude: Double, altitude: Double, time: Long) {
        this.latitude = latitude
        this.longitude = longitude
        this.altitude = altitude
        this.time = time
    }

    constructor(loc: Location) : this(loc.latitude, loc.longitude, loc.altitude, loc.time) { }
    constructor(loc: RunLocationData) : this(loc.latitude, loc.longitude, loc.altitude, loc.time) { }

    fun to(): LatLng {
        return LatLng(latitude, longitude)
    }
}
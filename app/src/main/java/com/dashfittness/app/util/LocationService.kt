package com.dashfittness.app.util

import android.annotation.SuppressLint
import android.app.*
import android.content.Intent
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Binder
import android.os.Bundle
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.dashfittness.app.R
import com.dashfittness.app.RunActivity

const val LOCATION_NOTIFICATION_ID = 17

class LocationService() : Service(), LocationListener {
    private lateinit var locMgr: LocationManager
    private var binder: IBinder? = null
    private var isStarted: Boolean = false

    override fun onCreate() { }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            "START_SERVICE" -> {
                if (isStarted == false) {
                    locMgr = applicationContext.getSystemService(LOCATION_SERVICE) as LocationManager
                    registerForegroundService()
                    startLocationUpdates()
                    isStarted = true
                }
            }
            "STOP_SERVICE" -> {
                if (isStarted == true) {
                    stopLocationUpdates()
                    stopForeground(true)
                    stopSelf()
                    isStarted = false
                }
            }
            "SHOW_PAUSE" -> {
                NotificationManagerCompat.from(this).notify(LOCATION_NOTIFICATION_ID, buildNotification(true, buildNotificationChannelId()));
            }
            "SHOW_RESUME" -> {
                NotificationManagerCompat.from(this).notify(LOCATION_NOTIFICATION_ID, buildNotification(false, buildNotificationChannelId()));
            }
        }
        return START_STICKY
    }

    private fun registerForegroundService() {
        var notification = buildNotification(true, buildNotificationChannelId())

        startForeground(LOCATION_NOTIFICATION_ID, notification)
    }

    private fun buildNotificationChannelId() : String {
        var channelId = "";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channelId = "location_service"
            var chan = NotificationChannel(channelId, "Location Service", NotificationManager.IMPORTANCE_HIGH)
            chan.lightColor = R.color.design_default_color_primary_dark
            chan.lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
            var service = getSystemService(NotificationManager::class.java);
            service.createNotificationChannel(chan);
        }
        return channelId
    }

    private fun buildNotification(showPause: Boolean, channelId: String): Notification {
        return NotificationCompat.Builder(this, channelId)
            .setContentTitle(resources.getString(R.string.app_name))
            .setContentText("You are running!")
            .setSmallIcon(R.drawable.ic_directions_run_black_24dp)
            .setContentIntent(buildIntentToShowRunActivity())
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setOngoing(true)
            .build()
    }

    private fun buildIntentToShowRunActivity(): PendingIntent {
        var notificationIntent = Intent(this, RunActivity::class.java)
        notificationIntent.action = "RUN_ACTIVITY"
        notificationIntent.putExtra("SERVICE_STARTED_KEY", true)

        return PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    @SuppressLint("MissingPermission")
    fun startLocationUpdates() {
        try {
            val criteria = Criteria()
            criteria.accuracy = Criteria.ACCURACY_FINE
            criteria.powerRequirement = Criteria.POWER_HIGH

            val locationProvider = locMgr.getBestProvider(criteria, true)

            locMgr.requestLocationUpdates(locationProvider!!, 1000L, 0F, this)

        } catch (ex: Exception) {}
    }

    fun stopLocationUpdates() {
        try {
            locMgr.removeUpdates(this)
        } catch (ex: Exception) {}
    }

    override fun onBind(intent: Intent): IBinder? {
        try {
            if (intent != null) {
                binder = LocationServiceBinder(this)
                return binder;
            }
        } catch (ex: Exception) {}
        return null
    }

    override fun onDestroy() {
        var notificationManager = getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(LOCATION_NOTIFICATION_ID)

        isStarted = false
        super.onDestroy()
    }

    override fun onLocationChanged(location: Location?) {
        try {
            val intent = Intent("LOCATION_CHANGED")
            intent.putExtra("LOCATION_DATA", location)
            sendBroadcast(intent)
        } catch (ex: Exception) {}
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        //TODO("Not yet implemented")
    }

    override fun onProviderEnabled(provider: String?) {
        //TODO("Not yet implemented")
    }

    override fun onProviderDisabled(provider: String?) {
        //TODO("Not yet implemented")
    }
}

class LocationServiceBinder(private val service: LocationService) : Binder()
{
    fun GetLocationServiceBinder(): LocationService? = service
}
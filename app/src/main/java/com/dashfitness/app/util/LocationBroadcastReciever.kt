package com.dashfitness.app.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.Location
import android.util.Log

class LocationBroadcastReceiver: BroadcastReceiver() {
    private val onLocationReceived = EventHandler<Location?>()
    val locationReceived = Event(onLocationReceived)
    private val locs = ArrayList<Location>()

    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            "LOCATION_CHANGED" -> {
                val loc = intent.getParcelableExtra("LOCATION_DATA") as Location?
                loc?.let {
                    Log.i("locationReceived_3", "lat: ${loc.latitude}, lon: ${loc.longitude}, accuracy: ${loc.accuracy}, totalCount: ${locs.count()}")
                    onLocationReceived(loc)
                    locs.add(loc)
                }
            }
        }
    }
}
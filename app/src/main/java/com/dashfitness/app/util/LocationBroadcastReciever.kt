package com.dashfitness.app.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.Location

class LocationBroadcastReceiver: BroadcastReceiver() {
    private val onLocationReceived = EventHandler<Location?>()
    val locationReceived = Event(onLocationReceived)

    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            "LOCATION_CHANGED" -> onLocationReceived(intent.getParcelableExtra("LOCATION_DATA"))
        }
    }
}
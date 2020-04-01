package com.dashfittness.app.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.Location

class Event<T>(private val eventHandler: EventHandler<T>) {
    operator fun plusAssign(handler: (T) -> Unit) { eventHandler.handlers.add(handler) }
}

class EventHandler<T> {
    val handlers = arrayListOf<((T) -> Unit)>()
    operator fun invoke(value: T) { for (handler in handlers) handler(value) }
}

class LocationBroadcastReceiver: BroadcastReceiver() {
    private val onLocationReceived = EventHandler<Location?>()
    val locationReceived = Event(onLocationReceived)

    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            "LOCATION_CHANGED" -> onLocationReceived(intent.getParcelableExtra("LOCATION_DATA"))
        }
    }
}
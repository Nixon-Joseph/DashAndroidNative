package com.dashfitness.app.util

const val MILLIS_IN_SECOND = 1000L
const val MILLIS_IN_SECOND_DOUBLE = 1000.0
const val SECONDS_IN_MINUTE = 60L
const val SECONDS_IN_MINUTE_DOUBLE = 60.0
const val MINUTES_IN_HOUR = 60L

class Event<T>(private val eventHandler: EventHandler<T>) {
    operator fun plusAssign(handler: (T) -> Unit) { eventHandler.handlers.add(handler) }
}

class EventHandler<T> {
    val handlers = arrayListOf<((T) -> Unit)>()
    operator fun invoke(value: T) { for (handler in handlers) handler(value) }
}

fun convertLongToTimeString(timeMillis: Long): String {
    val totalSeconds = timeMillis / MILLIS_IN_SECOND
    val totalMinutes = totalSeconds / SECONDS_IN_MINUTE
    val totalHours = totalMinutes / MINUTES_IN_HOUR
    var str = "";
    if (totalHours > 0L) {
        str = totalHours.toString().padStart(2, '0')
    }
    return str + "${(totalMinutes % SECONDS_IN_MINUTE).toString().padStart(2, '0')}:${(totalSeconds % SECONDS_IN_MINUTE).toString().padStart(2, '0')}"
}

const val PACE_TO_MILIS_MULTIPLIER = 60000.0
fun calculatePace(timeElapsed: Long, distance: Double): Long {
    return if (distance > 0.0) {
        ((((timeElapsed.toDouble() / MILLIS_IN_SECOND_DOUBLE) / SECONDS_IN_MINUTE_DOUBLE) / distance) * PACE_TO_MILIS_MULTIPLIER).toLong()
    } else {
        0
    }
}

fun calculateDistance(distanceInMeters: Double, isMetric: Boolean): Double {
    return distanceInMeters / if(isMetric) 1000.0 else 1609.344
}
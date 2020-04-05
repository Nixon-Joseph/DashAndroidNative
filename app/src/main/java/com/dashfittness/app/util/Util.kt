package com.dashfittness.app.util

class Event<T>(private val eventHandler: EventHandler<T>) {
    operator fun plusAssign(handler: (T) -> Unit) { eventHandler.handlers.add(handler) }
}

class EventHandler<T> {
    val handlers = arrayListOf<((T) -> Unit)>()
    operator fun invoke(value: T) { for (handler in handlers) handler(value) }
}

fun convertLongToTimeString(timeMillis: Long): String {
    val totalSeconds = timeMillis / 1000L
    val totalMinutes = totalSeconds / 60L
    val totalHours = totalMinutes / 60L
    var str = "";
    if (totalHours > 0L) {
        str = totalHours.toString().padStart(2, '0')
    }
    return str + "${(totalMinutes % 60L).toString().padStart(2, '0')}:${(totalSeconds % 60L).toString().padStart(2, '0')}"
}
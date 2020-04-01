package com.dashfittness.app.util

import android.content.Context
import android.content.Intent
import android.os.Build


fun Context.startForegroundServiceCompat(target: Class<*>, action: String) {
    Intent(this, target).apply {
        this.action = action
        startForegroundServiceCompat(this)
    }
}

fun Context.startForegroundServiceCompat(intent: Intent) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        startForegroundService(intent)
    } else {
        startService(intent)
    }
}
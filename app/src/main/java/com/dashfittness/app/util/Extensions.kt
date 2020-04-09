package com.dashfittness.app.util

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.View
import androidx.lifecycle.MutableLiveData


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

fun View.animateView(toVisibility: Int, toAlpha: Float, duration: Long) {
    val show = toVisibility == View.VISIBLE
    if (show) {
        alpha = 0f;
    }
    visibility = View.VISIBLE
    animate()
        .setDuration(duration)
        .alpha(if (show) toAlpha else 0f)
        .setListener(object: AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                visibility = toVisibility
            }
        })
}

fun <T> MutableLiveData<T>.notifyObservers() {
    this.value = this.value
}
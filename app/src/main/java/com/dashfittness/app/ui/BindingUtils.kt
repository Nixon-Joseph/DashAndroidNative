package com.dashfittness.app.ui

import androidx.databinding.BindingAdapter
import com.dashfittness.app.database.RunData
import com.dashfittness.app.util.convertLongToTimeString
import com.google.android.material.textview.MaterialTextView
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("timeElapsedString")
fun MaterialTextView.setTimeElapsedString(run: RunData) {
    run.let {
        text = "Run Time: ${convertLongToTimeString(run.endTimeMilli - run.startTimeMilli)}"
    }
}

@BindingAdapter("totalDistanceString")
fun MaterialTextView.setTotalDistanceString(run: RunData) {
    run.let {
        text = "Distance: ${String.format("%.1f", run.totalDistance)} Miles"
    }
}

@BindingAdapter("dateString")
fun MaterialTextView.setDateString(run: RunData) {
    run.let {
        text = "Date: ${getDate(run.startTimeMilli)}"
    }
}

private fun getDate(millis: Long) : String {
    val formatter = SimpleDateFormat.getDateTimeInstance()
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = millis
    return formatter.format(calendar.time)
}
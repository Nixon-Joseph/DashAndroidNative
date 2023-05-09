package com.dashfitness.app.ui

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.preference.PreferenceManager
import com.dashfitness.app.BaseApplication
import com.dashfitness.app.R
import com.dashfitness.app.database.RunData
import com.dashfitness.app.database.RunLocationData
import com.dashfitness.app.training.ITrainingRun
import com.dashfitness.app.training.TrainingRun
import com.dashfitness.app.ui.main.run.models.RunSegment
import com.dashfitness.app.ui.main.run.models.RunSegmentSpeed
import com.dashfitness.app.ui.main.run.models.RunSegmentType
import com.dashfitness.app.util.convertLongToTimeString
import com.google.android.material.textview.MaterialTextView
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList

@BindingAdapter("timeElapsedString")
fun MaterialTextView.setTimeElapsedString(run: RunData) {
    run.let {
        text = "Run Time: ${convertLongToTimeString(run.endTimeMilli - run.startTimeMilli)}"
    }
}

@BindingAdapter("totalDistanceString")
fun MaterialTextView.setTotalDistanceString(run: RunData?) {
    run?.let {
        val preferences = PreferenceManager.getDefaultSharedPreferences(BaseApplication.getAppContext())
        text = if (preferences.getBoolean("metric", false)) {
            "Distance: ${String.format("%.1f", run.totalDistance / 1000.0)} Kilometers"
        } else {
            "Distance: ${String.format("%.1f", run.totalDistance / 1609.344)} Miles"
        }
    }
}

@BindingAdapter("caloriesText")
fun MaterialTextView.setCaloriesText(run: RunData?) {
    run?.let {
        text = run.calories.toString()
    }
}

@BindingAdapter("totalDistance")
fun MaterialTextView.setTotalDistance(run: RunData?) {
    run?.let {
        val preferences = PreferenceManager.getDefaultSharedPreferences(BaseApplication.getAppContext())
        text = if (preferences.getBoolean("metric", false)) {
            "${String.format("%.1f", run.totalDistance / 1000.0)} Kilometers"
        } else {
            "${String.format("%.1f", run.totalDistance / 1609.344)} Miles"
        }
    }
}

@BindingAdapter("dateString")
fun MaterialTextView.setDateString(run: RunData) {
    run.let {
        text = "Date: ${getDate(run.startTimeMilli)}"
    }
}

@BindingAdapter("runSegmentText")
fun MaterialTextView.setText(segment: RunSegment) {
    segment.let {
        text = if (it.type === RunSegmentType.ALERT) {
            "\"${it.text}\""
        } else {
            val preferences = PreferenceManager.getDefaultSharedPreferences(BaseApplication.getAppContext())
            val distanceUnit = if (preferences.getBoolean("metric", false)) "Kilometer" else "Mile"
            "${it.speed} ${it.value} ${if(it.type == RunSegmentType.DISTANCE) "${distanceUnit}(s)" else "Minute(s)"}"
        }
    }
}

@BindingAdapter("trainingRunTitle")
fun MaterialTextView.setText(run: ITrainingRun) {
    run.let {
        text = it.Name
    }
}

@BindingAdapter("totalElevationChange")
fun MaterialTextView.setText(runLocs: List<RunLocationData>?) {
    runLocs?.let {
        var max = 0.0
        var min = 0.0
        var isFirstRun = true
        it.forEach {loc ->
            if (isFirstRun) {
                max = loc.altitude
                min = loc.altitude
                isFirstRun = false
            } else {
                max = max.coerceAtLeast(loc.altitude)
                min = min.coerceAtMost(loc.altitude)
            }
        }
        val preferences = PreferenceManager.getDefaultSharedPreferences(BaseApplication.getAppContext())
        var uom = "M"
        if (!preferences.getBoolean("metric", false)) { // convert to feet
            max /= 3.28084
            min /= 3.28084
            var uom = "Ft"
        }
        text = "${String.format("%.1f", max - min)} ${uom}"
    }
}

@BindingAdapter("runDetailDateTimes")
fun MaterialTextView.setText(run: RunData?) {
    run?.let {
        val use24HourFormat = android.text.format.DateFormat.is24HourFormat(BaseApplication.getAppContext())
        //April 8, 2023, 17:20 - 17:56
        val formatter = SimpleDateFormat("MMMM d, yyyy, ${if (use24HourFormat) "HH:mm" else "h:mm a" }")
        val startDate = formatter.format(Date(run.startTimeMilli))
        val formatter2 = SimpleDateFormat(if (use24HourFormat) "HH:mm" else "h:mm a")
        val endDate = formatter2.format(Date(run.endTimeMilli))
        text = "${startDate} - ${endDate}"
    }
}

@BindingAdapter("runTitle")
fun MaterialTextView.setTitleText(run: RunData?) {
    run?.let {
        text = it.title
    }
}

@BindingAdapter("runSegmentIcon")
fun AppCompatImageView.setSrc(segment: RunSegment) {
    segment.let {
        if (segment.type == RunSegmentType.ALERT) {
            setImageResource(R.drawable.baseline_priority_high_24)
        } else if (segment.speed == RunSegmentSpeed.RUN) {
            setImageResource(R.drawable.ic_directions_run_black_24dp)
        } else {
            setImageResource(R.drawable.ic_baseline_directions_walk_24)
        }
    }
}

@BindingAdapter("visibleWhenListEmpty")
fun View.setIsVisible(list: MutableList<RunSegment>?) {
    isVisible = list?.isEmpty() ?: true
}

private fun getDate(millis: Long) : String {
    val formatter = SimpleDateFormat.getDateTimeInstance()
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = millis
    return formatter.format(calendar.time)
}
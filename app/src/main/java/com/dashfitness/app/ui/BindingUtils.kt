package com.dashfitness.app.ui


import android.annotation.SuppressLint
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.preference.PreferenceManager
import com.dashfitness.app.DashApp
import com.dashfitness.app.R
import com.dashfitness.app.database.RunData
import com.dashfitness.app.database.RunLocationData
import com.dashfitness.app.training.ITrainingRun
import com.dashfitness.app.ui.main.run.models.RunSegment
import com.dashfitness.app.ui.main.run.models.RunSegmentSpeed
import com.dashfitness.app.ui.main.run.models.RunSegmentType
import com.dashfitness.app.util.convertLongToTimeString
import com.google.android.material.textview.MaterialTextView
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("timeElapsedString")
fun MaterialTextView.setTimeElapsedString(run: RunData) {
    run.let {
        text = DashApp.getString(R.string.timeElapsedString, convertLongToTimeString(run.endTimeMilli - run.startTimeMilli))
    }
}

@BindingAdapter("totalDistanceString")
fun MaterialTextView.setTotalDistanceString(run: RunData?) {
    run?.let {
        val preferences = PreferenceManager.getDefaultSharedPreferences(DashApp.getAppContext())
        text = if (preferences.getBoolean(DashApp.getString(R.string.metric_preference), false)) {
            DashApp.getString(R.string.totalDistanceStringM, run.totalDistance / 1000.0)
        } else {
            DashApp.getString(R.string.totalDistanceStringImp, run.totalDistance / 1609.344)
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
        val preferences = PreferenceManager.getDefaultSharedPreferences(DashApp.getAppContext())
        text = if (preferences.getBoolean(DashApp.getString(R.string.metric_preference), false)) {
            DashApp.getString(R.string.totalDistanceM, run.totalDistance / 1000.0)
        } else {
            DashApp.getString(R.string.totalDistanceImp, run.totalDistance / 1609.344)
        }
    }
}

@BindingAdapter("dateString")
fun MaterialTextView.setDateString(run: RunData) {
    run.let {
        text = DashApp.getString(R.string.dateString, getDate(run.startTimeMilli))
    }
}

@BindingAdapter("runSegmentText")
fun MaterialTextView.setText(segment: RunSegment) {
    segment.let {
        text = if (it.type === RunSegmentType.ALERT) {
            "\"${it.text}\""
        } else {
            val preferences = PreferenceManager.getDefaultSharedPreferences(DashApp.getAppContext())
            val distanceUnit = if (it.type == RunSegmentType.DISTANCE) {
                if (preferences.getBoolean(DashApp.getString(R.string.metric_preference), false)) {
                    "Kilometer"
                } else {
                    "Mile"
                }
            } else {
                "Minute"
            }
            DashApp.getString(R.string.runSegmentText, it.speed, it.value, distanceUnit)
        }
    }
}

@BindingAdapter("trainingRunTitle")
fun MaterialTextView.setText(run: ITrainingRun) {
    run.let {
        text = it.name
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
        val preferences = PreferenceManager.getDefaultSharedPreferences(DashApp.getAppContext())
        var uom = "M"
        if (!preferences.getBoolean(DashApp.getString(R.string.metric_preference), false)) { // convert to feet
            max /= 3.28084
            min /= 3.28084
            uom = "Ft"
        }
        text = DashApp.getString(R.string.totalElevationChange, max - min, uom)
    }
}

@SuppressLint("SimpleDateFormat")
@BindingAdapter("runDetailDateTimes")
fun MaterialTextView.setText(run: RunData?) {
    run?.let {
        val use24HourFormat = android.text.format.DateFormat.is24HourFormat(DashApp.getAppContext())
        //April 8, 2023, 17:20 - 17:56
        val formatter = SimpleDateFormat("MMMM d, yyyy, ${if (use24HourFormat) "HH:mm" else "h:mm a" }")
        val startDate = formatter.format(Date(run.startTimeMilli))
        val formatter2 = SimpleDateFormat(if (use24HourFormat) "HH:mm" else "h:mm a")
        val endDate = formatter2.format(Date(run.endTimeMilli))
        text = DashApp.getString(R.string.runDetailDateTimes, startDate, endDate)
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

@SuppressLint("SimpleDateFormat")
@BindingAdapter("trainingRunCompletedDate")
fun MaterialTextView.setTrainingRunCompletedDateText(run: ITrainingRun?) {
    run?.finishedRunStartDate?.let {
        val formatter = SimpleDateFormat("MMMM d, yyyy")
        val startDate = formatter.format(Date(it))
        text = DashApp.getString(R.string.trainingRunCompletedDate, startDate)
    }
}

@BindingAdapter("showTrainingRunCompletedDate")
fun MaterialTextView.setShowTrainingRunCompletedDate(run: ITrainingRun?) {
    visibility = View.INVISIBLE
    run?.finishedRunStartDate?.let {
        visibility = View.VISIBLE
    }
}

private fun getDate(millis: Long) : String {
    val formatter = SimpleDateFormat.getDateTimeInstance()
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = millis
    return formatter.format(calendar.time)
}
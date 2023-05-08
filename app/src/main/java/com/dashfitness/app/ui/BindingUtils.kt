package com.dashfitness.app.ui

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.dashfitness.app.R
import com.dashfitness.app.database.RunData
import com.dashfitness.app.training.ITrainingRun
import com.dashfitness.app.training.TrainingRun
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

@BindingAdapter("runSegmentText")
fun MaterialTextView.setText(segment: RunSegment) {
    segment.let {
        if (it.type === RunSegmentType.ALERT) {
            text = "\"${it.text}\""
        } else {
            text = "${it.speed} ${it.value} ${if(it.type == RunSegmentType.DISTANCE) "Mile(s)" else "Minute(s)"}"
        }
    }
}

@BindingAdapter("trainingRunTitle")
fun MaterialTextView.setText(run: ITrainingRun) {
    run.let {
        text = it.Name
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
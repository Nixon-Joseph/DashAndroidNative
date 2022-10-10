package com.dashfitness.app.ui.main.run.setup

import android.content.Context
import android.content.Intent
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dashfitness.app.R
import com.dashfitness.app.database.RunSegmentData
import com.dashfitness.app.ui.main.run.models.RunSegment
import com.dashfitness.app.ui.main.run.models.RunSegmentSpeed
import com.dashfitness.app.ui.main.run.models.RunSegmentType
import com.dashfitness.app.util.Event
import com.dashfitness.app.util.EventHandler

class RunSetupViewModel : ViewModel() {
    private var clicked = false

    val segments: MutableLiveData<MutableList<RunSegment>> = MutableLiveData()

    private val _navigateToRunActivity= MutableLiveData<Boolean>()
    val navigateToRunActivity: LiveData<Boolean>
        get() = _navigateToRunActivity;

    fun onRunClick() {
        _navigateToRunActivity.value = true;
    }

    fun onRunNavigated() {
        _navigateToRunActivity.value = false;
    }

    private val onAddSegment = EventHandler<Boolean>()
    val addSegmentClicked = Event(onAddSegment)

    fun onAddSegmentClicked() {
        onAddSegment.invoke(clicked)
        clicked = !clicked
    }

    private val onAddRunSegment = EventHandler<RunSegmentSpeed>()
    val addRunSegmentClicked = Event(onAddRunSegment)

    fun onAddRunSegmentClicked() {
        onAddRunSegment.invoke(RunSegmentSpeed.Run)
    }

    fun onAddWalkSegmentClicked() {
        onAddRunSegment.invoke(RunSegmentSpeed.Walk)
    }

    fun addSegment(type: RunSegmentType, speed: RunSegmentSpeed, value: Float) {
        val list = segments.value?.let { ArrayList(it) } ?: ArrayList()
        list.add(RunSegment(type, speed, value))
        segments.value = list
    }

    fun removeSegmentAt(position: Int) {
        val list = segments.value?.let { ArrayList(it) } ?: ArrayList()
        list.removeAt(position)
        segments.value = list
    }
}

package com.dashfitness.app.ui.main.run.setup

import android.content.res.Resources
import android.graphics.Color
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.dashfitness.app.R
import com.dashfitness.app.RunViewModel
import com.dashfitness.app.ui.main.run.models.RunSegment
import com.dashfitness.app.ui.main.run.models.RunSegmentSpeed
import com.dashfitness.app.ui.main.run.models.RunSegmentType
import com.dashfitness.app.util.Event
import com.dashfitness.app.util.EventHandler
import java.util.*

class RunSetupViewModel : ViewModel() {
    private var clicked = false
    var isCustom = MutableLiveData<Boolean>()

    init {
        isCustom.postValue(false)
    }

    val segments: MutableLiveData<MutableList<RunSegment>> = MutableLiveData()

    private val _navigateToRunActivity= MutableLiveData<Boolean>()
    val navigateToRunActivity: LiveData<Boolean>
        get() = _navigateToRunActivity

    fun onRunClick() {
        _navigateToRunActivity.value = true
    }

    fun onRunNavigated() {
        _navigateToRunActivity.value = false
    }

    private val onAddSegment = EventHandler<Boolean>()
    val addSegmentClicked = Event(onAddSegment)

    fun onAddSegmentClicked() {
        onAddSegment.invoke(clicked)
        clicked = !clicked
    }

    fun onCustomToggleClick() {
        isCustom.value?.let {
            if (!it) {
                isCustom.postValue(true)
            }
        }
    }

    fun onTrainingToggleClick() {
        isCustom.value?.let {
            if (it) {
                isCustom.postValue(false)
            }
        }
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

    fun editSegment(segmentId: UUID, segmentType: RunSegmentType, segmentSpeed: RunSegmentSpeed, value: Float) {
        val list = segments.value?.let { ArrayList(it) } ?: ArrayList()
        val indexOfSegment = list.indexOfFirst { it.id == segmentId }
        list.removeAt(indexOfSegment)
        list.add(indexOfSegment, RunSegment(segmentType, segmentSpeed, value))
        segments.value = list
    }
}

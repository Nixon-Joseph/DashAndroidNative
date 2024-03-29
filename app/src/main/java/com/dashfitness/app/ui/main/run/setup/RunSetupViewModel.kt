package com.dashfitness.app.ui.main.run.setup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dashfitness.app.ui.main.run.models.RunActivityModel
import com.dashfitness.app.ui.main.run.models.RunSegment
import com.dashfitness.app.ui.main.run.models.RunSegmentSpeed
import com.dashfitness.app.ui.main.run.models.RunSegmentType
import com.dashfitness.app.util.Event
import com.dashfitness.app.util.EventHandler
import java.util.*
import kotlin.collections.ArrayList

class RunSetupViewModel : ViewModel() {
    private var clicked = false
    var isCustom = MutableLiveData<Boolean>()
    var isTreadmill = MutableLiveData<Boolean>()

    init {
        isCustom.postValue(false)
        isTreadmill.postValue(false)
    }

    val segments: MutableLiveData<MutableList<RunSegment>> = MutableLiveData()

    private val _triggerCustomRunActivity= MutableLiveData<Boolean>()
    val triggerCustomRunActivity: LiveData<Boolean>
        get() = _triggerCustomRunActivity

    fun onCustomRunContinueClick() {
        _triggerCustomRunActivity.value = true
    }

    fun onCustomRunNavigated() {
        _triggerCustomRunActivity.value = false
    }

    private val _launchRunActivity= EventHandler<RunActivityModel>()
    val launchRunActivityEvent = Event(_launchRunActivity)

    fun launchRunActivity(runSegments: ArrayList<RunSegment>, planCode: String = "") {
        _launchRunActivity.invoke(RunActivityModel(runSegments, planCode))
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

    fun onOutdoorToggleClick() {
        isTreadmill.value?.let {
            if (it) {
                isTreadmill.postValue(false)
            }
        }
    }

    fun onTreadmillToggleClick() {
        isTreadmill.value?.let {
            if (!it) {
                val segmentList = segments.value
                if (!segmentList.isNullOrEmpty() && segmentList.any{ s -> s.type === RunSegmentType.DISTANCE}) {
                    onShowTreadmillDistanceSegmentsAlert.invoke(true)
                } else {
                    isTreadmill.postValue(true)
                }
            }
        }
    }

    private val onAddRunSegment = EventHandler<RunSegmentSpeed>()
    val addRunSegmentClicked = Event(onAddRunSegment)
    private val onAddRunAlertSegment = EventHandler<Boolean>()
    val addRunAlertSegmentClicked = Event(onAddRunAlertSegment)
    private val onShowTreadmillDistanceSegmentsAlert = EventHandler<Boolean>()
    val showTreadmillDistanceSegmentsAlert = Event(onShowTreadmillDistanceSegmentsAlert)

    fun onAddRunSegmentClicked() {
        onAddRunSegment.invoke(RunSegmentSpeed.RUN)
    }

    fun onAddWalkSegmentClicked() {
        onAddRunSegment.invoke(RunSegmentSpeed.WALK)
    }

    fun onAddAlertSegmentClicked() {
        onAddRunAlertSegment.invoke(true)
    }

    fun addSegment(type: RunSegmentType, speed: RunSegmentSpeed, value: Float, text: String? = null, isCustomText: Boolean = false) {
        val list = segments.value?.let { ArrayList(it) } ?: ArrayList()
        list.add(RunSegment(type, speed, value, text, isCustomText))
        segments.value = list
    }

    fun removeDistanceSegments() {
        val list = segments.value?.let { ArrayList(it) } ?: ArrayList()
        list.removeAll { segment -> segment.type === RunSegmentType.DISTANCE }
        segments.value = list
    }

    fun removeSegmentAt(position: Int) {
        val list = segments.value?.let { ArrayList(it) } ?: ArrayList()
        list.removeAt(position)
        segments.value = list
    }

    fun editSegment(segmentId: UUID, segmentType: RunSegmentType, segmentSpeed: RunSegmentSpeed, value: Float, text: String? = null, isCustomText: Boolean = false) {
        val list = segments.value?.let { ArrayList(it) } ?: ArrayList()
        val indexOfSegment = list.indexOfFirst { it.id == segmentId }
        list.removeAt(indexOfSegment)
        list.add(indexOfSegment, RunSegment(segmentType, segmentSpeed, value, text, isCustomText))
        segments.value = list
    }
}

package com.dashfitness.app

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.dashfitness.app.util.*

class RunViewModel : ViewModel() {
    private val _timeElapsedString = MutableLiveData<String>()
    val timeElapsedString: LiveData<String>
        get() = _timeElapsedString
    private val _totalDistanceString = MutableLiveData<String>()
    val totalDistanceString: LiveData<String>
        get() = _totalDistanceString
    private val _averagePaceString = MutableLiveData<String>()
    val averagePaceString: LiveData<String>
        get() = _averagePaceString
    private val _caloriesBurntString = MutableLiveData<String>()
    val caloriesBurntString: LiveData<String>
        get() = _caloriesBurntString
    private val _elevationChangeString = MutableLiveData<String>()
    val elevationChangeString: LiveData<String>
        get() = _elevationChangeString
    private val _elevationTypeString = MutableLiveData<String>()
    val elevationTypeString: LiveData<String>
        get() = _elevationTypeString
    private val _distanceTypeString = MutableLiveData<String>()
    val distanceTypeString: LiveData<String>
        get() = _distanceTypeString

    val runState = MutableLiveData<RunStates>()

    private val onEndRun = EventHandler<Boolean>()
    val endRun = Event(onEndRun)
    private val onCancelRun = EventHandler<Boolean>()
    val cancelRun = Event(onCancelRun)
    private val onStartRun = EventHandler<Boolean>()
    val startRun = Event(onStartRun)
    private val onResumeRun = EventHandler<Boolean>()
    val resumeRun = Event(onResumeRun)
    private val onPauseRun = EventHandler<Boolean>()
    val pauseRun = Event(onPauseRun)

    private var timeElapsed = 0L
    private var totalDistance: Double = 0.0
    var isMetric: Boolean = false

    val startRunVisibility = Transformations.map(runState) {
        when (it) {
            RunStates.Unstarted -> View.VISIBLE
            else -> View.GONE
        }
    }
    val restartRunVisibility = Transformations.map(runState) {
        when (it) {
            RunStates.Paused -> View.VISIBLE
            else -> View.GONE
        }
    }
    val cancelRunVisibility = Transformations.map(runState) {
        when (it) {
            RunStates.Unstarted -> View.VISIBLE
            else -> View.GONE
        }
    }
    val endRunVisibility = Transformations.map(runState) {
        when (it) {
            RunStates.Paused, RunStates.Finished -> View.VISIBLE
            else -> View.GONE
        }
    }
    val pauseRunVisibility = Transformations.map(runState) {
        when (it) {
            RunStates.Running -> View.VISIBLE
            else -> View.GONE
        }
    }

    init {
        _timeElapsedString.value = "00:00"
        _totalDistanceString.value = "0.0"
        _averagePaceString.value = "00:00"
        _caloriesBurntString.value = "0"
        _elevationChangeString.value = "0"
        _elevationTypeString.value = "ft"
        _distanceTypeString.value = "miles"
        runState.value = RunStates.Unstarted
    }

    fun updateTimeElapsed(elapsedTimeInMillis: Long) {
        timeElapsed = elapsedTimeInMillis
        _timeElapsedString.postValue(convertLongToTimeString(elapsedTimeInMillis))
    }

    fun updateTotalDistance(totalDistanceInMeters: Double) {
        totalDistance = calculateDistance(totalDistanceInMeters, isMetric)
        _totalDistanceString.postValue(String.format("%.1f", totalDistance))
        val averagePace = calculatePace(timeElapsed, totalDistance)
        _averagePaceString.postValue(if (averagePace > 0) convertLongToTimeString(averagePace) else "∞")
        val caloriesBurnt = ((timeElapsed.toDouble() / MILLIS_IN_SECOND / SECONDS_IN_MINUTE) * (2.5 * 1.3)).toInt()
        _caloriesBurntString.postValue(caloriesBurnt.toString())
    }

    fun updateElevationChange(elevation: Double) {
        _elevationChangeString.postValue(String.format("%.1f", elevation))
    }
    fun onEndRunClicked() {
        onEndRun.invoke(true)
    }

    fun onCancelClicked() {
        onCancelRun.invoke(true)
    }

    fun onPauseClicked() {
        onPauseRun.invoke(true)
    }

    fun onRestartClicked() {
        onResumeRun.invoke(true)
    }

    fun onStartClicked() {
        onStartRun.invoke(true)
    }

    enum class RunStates {
        Unstarted,
        Running,
        Paused,
        Finished
    }
}
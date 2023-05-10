package com.dashfitness.app.ui.viewmodels

import android.content.SharedPreferences
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
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
    private var isMetric: Boolean = false
    private var calculateCalories: Boolean = false
    private var age: Int = 0
    private var weight: Double = 0.0
    private var height: Double = 0.0

    val startRunVisibility = runState.map {
        when (it) {
            RunStates.Unstarted -> View.VISIBLE
            else -> View.GONE
        }
    }
    val restartRunVisibility = runState.map {
        when (it) {
            RunStates.Paused -> View.VISIBLE
            else -> View.GONE
        }
    }
    val cancelRunVisibility = runState.map {
        when (it) {
            RunStates.Unstarted -> View.VISIBLE
            else -> View.GONE
        }
    }
    val endRunVisibility = runState.map {
        when (it) {
            RunStates.Paused, RunStates.Finished -> View.VISIBLE
            else -> View.GONE
        }
    }
    val pauseRunVisibility = runState.map {
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
        if (calculateCalories) {
            val caloriesBurnt = estimateCaloriesBurned(totalDistanceInMeters, timeElapsed)
            _caloriesBurntString.postValue(caloriesBurnt.toString())
        }
    }

    fun estimateCaloriesBurned(
        distance: Double,
        timeSpent: Long // in milliseconds
    ): Int {
        // Convert weight and height to metric units if needed
        val weightKg = if (isMetric) weight else weight * 0.453592
        val heightCm = if (isMetric) height else height * 2.54

        // Calculate speed in m/s
        val timeSpentSeconds = timeSpent / 1000.0
        val speed = distance / timeSpentSeconds

        // Calculate the approximate MET value based on running speed
        val met = when {
            speed < 1.341 -> 6.0 // walking
            speed < 2.682 -> 9.8 // jogging
            else -> 11.8 // running
        }

        // Calculate the Basal Metabolic Rate (BMR) using the Mifflin-St Jeor equation
        val bmr = 10 * weightKg + 6.25 * heightCm - 5 * age + 5

        // Calculate calories burned
        val caloriesBurnedPerMinute = bmr / 24 / 60 * met
        val timeSpentMinutes = timeSpentSeconds / 60.0
        val totalCaloriesBurned = caloriesBurnedPerMinute * timeSpentMinutes

        return totalCaloriesBurned.toInt()
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

    fun setupRun(preferences: SharedPreferences) {
        isMetric = preferences.getBoolean("metric", false)
        calculateCalories = preferences.getBoolean("estimate_calories", false)
        if (calculateCalories) {
            preferences.getString("age", "0")?.let {
                age = it.toInt()
            }
            if (isMetric) {
                preferences.getString("weight_kilo", "0.0")?.let {
                    weight = it.toDouble()
                }
                preferences.getString("height_cm", "0.0")?.let {
                    height = it.toDouble()
                }
            } else {
                preferences.getString("weight_lbs", "0.0")?.let {
                    weight = it.toDouble()
                }
                preferences.getString("height_feet", "0.0")?.let {ft ->
                    preferences.getString("height_inches", "0.0")?.let {inch ->
                        height = ft.toDouble() * 12.0 + inch.toDouble()
                    }
                }
            }
            if (weight == 0.0 || height == 0.0 || age == 0) {
                calculateCalories = false
            }
        }
    }

    enum class RunStates {
        Unstarted,
        Running,
        Paused,
        Finished
    }
}
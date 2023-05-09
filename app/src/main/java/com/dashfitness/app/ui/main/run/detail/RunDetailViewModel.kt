package com.dashfitness.app.ui.main.run.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dashfitness.app.database.RunData
import com.dashfitness.app.database.RunDatabaseDao
import com.dashfitness.app.database.RunLocationData
import com.dashfitness.app.database.RunSegmentData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

class RunDetailViewModel(
    database: RunDatabaseDao,
    runId: Long
) : ViewModel() {
    val run: LiveData<RunData>
    val segments: LiveData<List<RunSegmentData>>
    val runLocs: LiveData<List<RunLocationData>>
    var totalDistance = MutableLiveData(0.0)

    init {
        run = database.getRun(runId)
        segments = database.getRunSegments(runId)
        runLocs = database.getRunLocations(runId)
    }
}
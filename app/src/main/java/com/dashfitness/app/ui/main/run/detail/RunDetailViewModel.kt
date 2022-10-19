package com.dashfitness.app.ui.main.run.detail

import androidx.lifecycle.ViewModel
import com.dashfitness.app.database.RunDatabaseDao
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

class RunDetailViewModel(
    database: RunDatabaseDao,
    runId: Long
) : ViewModel() {
    val segments = database.getRunSegments(runId)
    val runLocs = database.getRunLocations(runId)
}
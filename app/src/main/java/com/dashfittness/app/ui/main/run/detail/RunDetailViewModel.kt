package com.dashfittness.app.ui.main.run.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dashfittness.app.database.RunDatabaseDao
import com.dashfittness.app.database.RunSegmentData
import com.dashfittness.app.util.DBViewModel

class RunDetailViewModel(database: RunDatabaseDao, runId: Long) : DBViewModel(database) {
    val segments = database.getRunSegments(runId)
    val runLocs = database.getRunLocations(runId)
}
package com.dashfitness.app.ui.main.run.detail

import com.dashfitness.app.database.RunDatabaseDao
import com.dashfitness.app.util.DBViewModel

class RunDetailViewModel(database: RunDatabaseDao, runId: Long) : DBViewModel(database) {
    val segments = database.getRunSegments(runId)
    val runLocs = database.getRunLocations(runId)
}
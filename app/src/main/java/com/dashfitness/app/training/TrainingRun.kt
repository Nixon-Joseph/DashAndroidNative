package com.dashfitness.app.training

import com.dashfitness.app.database.RunData
import com.dashfitness.app.ui.main.run.models.RunSegment
import java.io.Serializable

interface ITrainingRun {
    var Code: String
    var Name: String
    var Summary: String
    var FinishedRun: RunData?
    fun getRunSegments(): ArrayList<RunSegment>
}

abstract class TrainingRun(code: String, name: String, summary: String, finishedRun: RunData? = null) : ITrainingRun, Serializable {
}
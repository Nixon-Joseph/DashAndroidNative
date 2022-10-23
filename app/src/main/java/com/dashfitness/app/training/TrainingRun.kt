package com.dashfitness.app.training

import com.dashfitness.app.ui.main.run.models.RunSegment
import java.io.Serializable

interface ITrainingRun {
    var Code: String
    var Name: String
    var Summary: String
    fun getRunSegments(): ArrayList<RunSegment>
}

abstract class TrainingRun : ITrainingRun, Serializable {
    constructor(
        code: String,
        name: String,
        summary: String
    ) {
        Code = code
        Name = name
        Summary = summary
    }
}
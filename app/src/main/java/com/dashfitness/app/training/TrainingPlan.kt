package com.dashfitness.app.training

import java.io.Serializable

abstract class TrainingPlan(
    var Code: String,
    var Name: String,
    var Description: String,
    var Runs: ArrayList<ITrainingRun>
) : Serializable
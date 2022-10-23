package com.dashfitness.app.training

abstract class TrainingPlan(
    var Code: String,
    var Name: String,
    var Description: String,
    var Runs: ArrayList<ITrainingRun>
) : java.io.Serializable
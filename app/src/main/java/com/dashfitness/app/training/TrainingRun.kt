package com.dashfitness.app.training

import com.dashfitness.app.services.TrackingService.Companion.totalDistance
import com.dashfitness.app.ui.main.run.models.RunSegment
import java.io.Serializable

interface ITrainingRun {
    var code: String
    var name: String
    var summary: String
    var finishedRunStartDate: Long?
    fun getRunSegments(): ArrayList<RunSegment>

    override operator fun equals(other: Any?): Boolean
}

abstract class TrainingRun(code: String, name: String, summary: String, finishedRunStartDate: Long? = null) : ITrainingRun, Serializable {
    override fun equals(
        other: Any?
    ): Boolean {
        other?.let {
            return hashCode() == it.hashCode()
        }
        return false
    }

    override fun hashCode(): Int {
        var result = TrainingRun::class.java.name.hashCode()
        result = 31 * result + code.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + summary.hashCode()
        result = 31 * result + totalDistance.hashCode()
        finishedRunStartDate?.let {
            result = 31 * result + finishedRunStartDate.hashCode()
        }
        return result
    }
}
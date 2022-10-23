package com.dashfitness.app.training

import com.dashfitness.app.ui.main.run.models.RunSegment
import com.dashfitness.app.ui.main.run.models.RunSegmentSpeed
import java.io.Serializable

// Shamelessly pulled from https://marathonhandbook.com
class FiveKActive: TrainingPlan(
    CODE,
    NAME,
    DESCRIPTION,
    arrayListOf(
        FiveKActiveTrainingRun("w1d1", "Week 1 Day 1", "1 min running / 2 min walking, for 30 minutes."),
        FiveKActiveTrainingRun("w1d2", "Week 1 Day 2", "1 min running / 2 min walking, for 30 minutes."),
        FiveKActiveTrainingRun("w1d3", "Week 1 Day 3", "12 min running."),
        FiveKActiveTrainingRun("w2d1", "Week 2 Day 1", "1 min running / 1 min walking, for 30 minutes."),
        FiveKActiveTrainingRun("w2d2", "Week 2 Day 2", "1 min running / 1 min walking, for 30 minutes."),
        FiveKActiveTrainingRun("w2d3", "Week 2 Day 3", "18 min running."),
        FiveKActiveTrainingRun("w3d1", "Week 3 Day 1", "1.5 min running / 0.5 min walking, for 30 minutes."),
        FiveKActiveTrainingRun("w3d2", "Week 3 Day 2", "1.5 min running / 0.5 min walking, for 30 minutes."),
        FiveKActiveTrainingRun("w3d3", "Week 3 Day 3", "26 min running"),
        FiveKActiveTrainingRun("w4d1", "Week 4 Day 1", "2 min run / 1 min walk, for 30 minutes."),
        FiveKActiveTrainingRun("w4d2", "Week 4 Day 2", "2 min run / 1 min walk, for 30 minutes."),
        FiveKActiveTrainingRun("w4d3", "Week 4 Day 3", "Run your 5K!"),
    )
), Serializable {
    companion object {
        const val CODE: String = "5ka"
        const val NAME: String = "5K Active"
        const val DESCRIPTION: String = "Running 5k – around 3.1 miles – is an excellent measure of your running fitness.\n\n" +
                "Beginners use the 5 kilometer distance as a goal to reach, while experienced runners use their 5k time as a measure of their running fitness.\n\n" +
                "This plan is for those who are athletic or more experienced.\n\n" +
                "If you are a beginner or non/less-active, you might want to consider the \"5K Beginner\" run plan.\n\n" +
                "Credit: https://marathonhandbook.com - review for more information"
    }
}

class FiveKActiveTrainingRun(
     override var Code: String,
     override var Name: String,
     override var Summary: String
) : TrainingRun(Code, Name, Summary) {
    companion object {
        private fun _getRunSegments(code: String): ArrayList<RunSegment> {
            return when (code) {
                "w1d1" -> arrayListOf(
                    RunSegment.Time(RunSegmentSpeed.WARM_UP, 5f), // warm up
                    RunSegment.Time(RunSegmentSpeed.RUN, 2f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 1f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 2f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 1f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 2f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 1f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 2f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 1f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 2f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 1f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 2f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 1f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 2f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 1f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 2f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 1f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 2f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 1f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 2f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 1f),
                    RunSegment.Time(RunSegmentSpeed.COOL_DOWN, 5f), // cool down
                )
                "w1d2" -> arrayListOf(
                    RunSegment.Time(RunSegmentSpeed.WARM_UP, 5f), // warm up
                    RunSegment.Time(RunSegmentSpeed.RUN, 1f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 2f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 2f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 2f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 2f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 2f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 2f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 2f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 2f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 2f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 2f),
                    RunSegment.Time(RunSegmentSpeed.COOL_DOWN, 5f), // cool down
                )
                "w1d3" -> arrayListOf(
                    RunSegment.Time(RunSegmentSpeed.WARM_UP, 5f), // warm up
                    RunSegment.Time(RunSegmentSpeed.RUN, 12f),
                    RunSegment.Time(RunSegmentSpeed.COOL_DOWN, 5f), // cool down
                )
                "w2d1" -> arrayListOf(
                    RunSegment.Time(RunSegmentSpeed.WARM_UP, 5f), // warm up
                    RunSegment.Time(RunSegmentSpeed.RUN, 1f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 1f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 1f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 1f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 1f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 1f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 1f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 1f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 1f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 1f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 1f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 1f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 1f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 1f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 1f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 1f),
                    RunSegment.Time(RunSegmentSpeed.COOL_DOWN, 5f), // cool down
                )
                "w2d2" -> arrayListOf(
                    RunSegment.Time(RunSegmentSpeed.WARM_UP, 5f), // warm up
                    RunSegment.Time(RunSegmentSpeed.RUN, 1f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 1f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 1f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 1f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 1f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 1f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 1f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 1f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 1f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 1f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 1f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 1f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 1f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 1f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 1f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 1f),
                    RunSegment.Time(RunSegmentSpeed.COOL_DOWN, 5f), // cool down
                )
                "w2d3" -> arrayListOf(
                    RunSegment.Time(RunSegmentSpeed.WARM_UP, 5f), // warm up
                    RunSegment.Time(RunSegmentSpeed.RUN, 18f),
                    RunSegment.Time(RunSegmentSpeed.COOL_DOWN, 5f), // cool down
                )
                "w3d1" -> arrayListOf(
                    RunSegment.Time(RunSegmentSpeed.WARM_UP, 5f), // warm up
                    RunSegment.Time(RunSegmentSpeed.RUN, 1.5f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 0.5f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1.5f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 0.5f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1.5f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 0.5f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1.5f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 0.5f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1.5f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 0.5f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1.5f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 0.5f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1.5f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 0.5f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1.5f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 0.5f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1.5f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 0.5f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1.5f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 0.5f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1.5f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 0.5f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1.5f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 0.5f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1.5f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 0.5f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1.5f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 0.5f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1.5f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 0.5f),
                    RunSegment.Time(RunSegmentSpeed.COOL_DOWN, 5f), // cool down
                )
                "w3d2" -> arrayListOf(
                    RunSegment.Time(RunSegmentSpeed.WARM_UP, 5f), // warm up
                    RunSegment.Time(RunSegmentSpeed.RUN, 1.5f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 0.5f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1.5f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 0.5f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1.5f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 0.5f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1.5f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 0.5f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1.5f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 0.5f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1.5f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 0.5f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1.5f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 0.5f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1.5f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 0.5f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1.5f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 0.5f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1.5f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 0.5f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1.5f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 0.5f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1.5f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 0.5f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1.5f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 0.5f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1.5f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 0.5f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1.5f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 0.5f),
                    RunSegment.Time(RunSegmentSpeed.COOL_DOWN, 5f), // cool down
                )
                "w3d3" -> arrayListOf(
                    RunSegment.Time(RunSegmentSpeed.WARM_UP, 5f), // warm up
                    RunSegment.Time(RunSegmentSpeed.RUN, 26f),
                    RunSegment.Time(RunSegmentSpeed.COOL_DOWN, 5f), // cool down
                )
                "w4d1" -> arrayListOf(
                    RunSegment.Time(RunSegmentSpeed.WARM_UP, 5f), // warm up
                    RunSegment.Time(RunSegmentSpeed.RUN, 2f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 1f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 2f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 1f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 2f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 1f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 2f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 1f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 2f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 1f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 2f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 1f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 2f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 1f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 2f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 1f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 2f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 1f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 2f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 1f),
                    RunSegment.Time(RunSegmentSpeed.COOL_DOWN, 5f), // cool down
                )
                "w4d3" -> arrayListOf(
                    RunSegment.Time(RunSegmentSpeed.WARM_UP, 5f), // warm up
                    RunSegment.Time(RunSegmentSpeed.RUN, 5f), // 5K
                    RunSegment.Time(RunSegmentSpeed.COOL_DOWN, 5f), // cool down
                )
                else -> throw java.lang.Exception("Could not find run")
            }
        }
    }

    override fun getRunSegments(): ArrayList<RunSegment> { return _getRunSegments(Code) }
}
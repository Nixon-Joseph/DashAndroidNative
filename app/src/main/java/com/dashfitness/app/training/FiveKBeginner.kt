package com.dashfitness.app.training

import com.dashfitness.app.ui.main.run.models.RunSegment
import com.dashfitness.app.ui.main.run.models.RunSegmentSpeed

// Shamelessly pulled from https://marathonhandbook.com
class FiveKBeginner: TrainingPlan(
    CODE,
    NAME,
    DESCRIPTION,
    arrayListOf(
        FiveKTrainingRun("w1d1", "Week 1 Day 1", "Lorem Ipsum"),
        FiveKTrainingRun("w1d2", "Week 1 Day 2", "Lorem Ipsum"),
        FiveKTrainingRun("w1d3", "Week 1 Day 3", "Lorem Ipsum"),
        FiveKTrainingRun("w2d1", "Week 2 Day 1", "Lorem Ipsum"),
        FiveKTrainingRun("w2d2", "Week 2 Day 2", "Lorem Ipsum"),
        FiveKTrainingRun("w2d3", "Week 2 Day 3", "Lorem Ipsum"),
        FiveKTrainingRun("w3d1", "Week 3 Day 1", "Lorem Ipsum"),
        FiveKTrainingRun("w3d2", "Week 3 Day 2", "Lorem Ipsum"),
        FiveKTrainingRun("w3d3", "Week 3 Day 3", "Lorem Ipsum"),
        FiveKTrainingRun("w4d1", "Week 4 Day 1", "Lorem Ipsum"),
        FiveKTrainingRun("w4d2", "Week 4 Day 2", "Lorem Ipsum"),
        FiveKTrainingRun("w4d3", "Week 4 Day 3", "Lorem Ipsum"),
        FiveKTrainingRun("w5d1", "Week 5 Day 1", "Lorem Ipsum"),
        FiveKTrainingRun("w5d2", "Week 5 Day 2", "Lorem Ipsum"),
        FiveKTrainingRun("w5d3", "Week 5 Day 3", "Lorem Ipsum"),
        FiveKTrainingRun("w6d1", "Week 6 Day 1", "Lorem Ipsum"),
        FiveKTrainingRun("w6d2", "Week 6 Day 2", "Lorem Ipsum"),
        FiveKTrainingRun("w6d3", "Week 6 Day 3", "Lorem Ipsum"),
        FiveKTrainingRun("w7d1", "Week 7 Day 1", "Lorem Ipsum"),
        FiveKTrainingRun("w7d2", "Week 7 Day 2", "Lorem Ipsum"),
        FiveKTrainingRun("w7d3", "Week 7 Day 3", "Lorem Ipsum"),
        FiveKTrainingRun("w8d1", "Week 8 Day 1", "Lorem Ipsum"),
        FiveKTrainingRun("w8d2", "Week 8 Day 2", "Lorem Ipsum"),
        FiveKTrainingRun("w8d3", "Week 8 Day 3", "Lorem Ipsum"),
    )
) {
    companion object {
        const val CODE: String = "5kb"
        const val NAME: String = "5K Beginner"
        const val DESCRIPTION: String = "Lorem Ipsum"
    }
}

class FiveKTrainingRun(
    override var Code: String,
    override var Name: String,
    override var Summary: String
) : TrainingRun(Code, Name, Summary) {
    companion object {
        private fun _getRunSegments(code: String): ArrayList<RunSegment> {
            return when (code) {
                "w1d1" -> arrayListOf(
                    RunSegment.Time(RunSegmentSpeed.WARM_UP, 5f), // warm up
                    RunSegment.Time(RunSegmentSpeed.RUN, 1f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 1.5f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 1.5f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 1.5f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 1.5f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 1.5f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 1.5f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 1.5f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 1.5f),
                    RunSegment.Time(RunSegmentSpeed.COOL_DOWN, 5f), // cool down
                )
                "w1d2" -> arrayListOf(
                    RunSegment.Time(RunSegmentSpeed.WARM_UP, 5f), // warm up
                    RunSegment.Time(RunSegmentSpeed.RUN, 1f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 1.5f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 1.5f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 1.5f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 1.5f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 1.5f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 1.5f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 1.5f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 1.5f),
                    RunSegment.Time(RunSegmentSpeed.COOL_DOWN, 5f), // cool down
                )
                "w1d3" -> arrayListOf(
                    RunSegment.Time(RunSegmentSpeed.WARM_UP, 5f), // warm up
                    RunSegment.Time(RunSegmentSpeed.RUN, 1f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 1.5f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 1.5f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 1.5f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 1.5f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 1.5f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 1.5f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 1.5f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 1.5f),
                    RunSegment.Time(RunSegmentSpeed.COOL_DOWN, 5f), // cool down
                )
                "w2d1" -> arrayListOf(
                    RunSegment.Time(RunSegmentSpeed.WARM_UP, 5f), // warm up
                    RunSegment.Time(RunSegmentSpeed.RUN, 1.5f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 2f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1.5f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 2f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1.5f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 2f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1.5f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 2f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1.5f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 2f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1.5f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 2f),
                    RunSegment.Time(RunSegmentSpeed.COOL_DOWN, 5f), // cool down
                )
                "w2d2" -> arrayListOf(
                    RunSegment.Time(RunSegmentSpeed.WARM_UP, 5f), // warm up
                    RunSegment.Time(RunSegmentSpeed.RUN, 1.5f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 2f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1.5f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 2f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1.5f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 2f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1.5f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 2f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1.5f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 2f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1.5f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 2f),
                    RunSegment.Time(RunSegmentSpeed.COOL_DOWN, 5f), // cool down
                )
                "w2d3" -> arrayListOf(
                    RunSegment.Time(RunSegmentSpeed.WARM_UP, 5f), // warm up
                    RunSegment.Time(RunSegmentSpeed.RUN, 1.5f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 2f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1.5f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 2f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1.5f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 2f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1.5f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 2f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1.5f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 2f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 1.5f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 2f),
                    RunSegment.Time(RunSegmentSpeed.COOL_DOWN, 5f), // cool down
                )
                "w3d1" -> arrayListOf(
                    RunSegment.Time(RunSegmentSpeed.WARM_UP, 5f), // warm up
                    RunSegment.Time(RunSegmentSpeed.RUN, 2f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 2f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 2f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 2f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 2f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 2f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 2f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 2f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 2f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 2f),
                    RunSegment.Time(RunSegmentSpeed.COOL_DOWN, 5f), // cool down
                )
                "w3d2" -> arrayListOf(
                    RunSegment.Time(RunSegmentSpeed.WARM_UP, 5f), // warm up
                    RunSegment.Time(RunSegmentSpeed.RUN, 2.5f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 2.5f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 2.5f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 2.5f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 2.5f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 2.5f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 2.5f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 2.5f),
                    RunSegment.Time(RunSegmentSpeed.COOL_DOWN, 5f), // cool down
                )
                "w3d3" -> arrayListOf(
                    RunSegment.Time(RunSegmentSpeed.WARM_UP, 5f), // warm up
                    RunSegment.Time(RunSegmentSpeed.RUN, 2.5f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 2.5f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 2.5f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 2.5f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 2.5f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 2.5f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 2.5f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 2.5f),
                    RunSegment.Time(RunSegmentSpeed.COOL_DOWN, 5f), // cool down
                )
                "w4d1" -> arrayListOf(
                    RunSegment.Time(RunSegmentSpeed.WARM_UP, 5f), // warm up
                    RunSegment.Time(RunSegmentSpeed.RUN, 3f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 2f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 3f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 2f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 3f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 2f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 3f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 2f),
                    RunSegment.Time(RunSegmentSpeed.COOL_DOWN, 5f), // cool down
                )
                "w4d2" -> arrayListOf(
                    RunSegment.Time(RunSegmentSpeed.WARM_UP, 5f), // warm up
                    RunSegment.Time(RunSegmentSpeed.RUN, 3f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 2f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 3f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 2f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 3f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 2f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 3f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 2f),
                    RunSegment.Time(RunSegmentSpeed.COOL_DOWN, 5f), // cool down
                )
                "w4d3" -> arrayListOf(
                    RunSegment.Time(RunSegmentSpeed.WARM_UP, 5f), // warm up
                    RunSegment.Time(RunSegmentSpeed.RUN, 4f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 2.5f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 4f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 2.5f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 4f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 2.5f),
                    RunSegment.Time(RunSegmentSpeed.COOL_DOWN, 5f), // cool down
                )
                "w5d1" -> arrayListOf(
                    RunSegment.Time(RunSegmentSpeed.WARM_UP, 5f), // warm up
                    RunSegment.Time(RunSegmentSpeed.RUN, 5f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 3f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 5f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 3f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 5f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 3f),
                    RunSegment.Time(RunSegmentSpeed.COOL_DOWN, 5f), // cool down
                )
                "w5d2" -> arrayListOf(
                    RunSegment.Time(RunSegmentSpeed.WARM_UP, 5f), // warm up
                    RunSegment.Time(RunSegmentSpeed.RUN, 8f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 5f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 8f),
                    RunSegment.Time(RunSegmentSpeed.COOL_DOWN, 5f), // cool down
                )
                "w5d3" -> arrayListOf(
                    RunSegment.Time(RunSegmentSpeed.WARM_UP, 5f), // warm up
                    RunSegment.Time(RunSegmentSpeed.RUN, 20f),
                    RunSegment.Time(RunSegmentSpeed.COOL_DOWN, 5f), // cool down
                )
                "w6d1" -> arrayListOf(
                    RunSegment.Time(RunSegmentSpeed.WARM_UP, 5f), // warm up
                    RunSegment.Time(RunSegmentSpeed.RUN, 6f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 3f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 6f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 3f),
                    RunSegment.Time(RunSegmentSpeed.COOL_DOWN, 5f), // cool down
                )
                "w6d2" -> arrayListOf(
                    RunSegment.Time(RunSegmentSpeed.WARM_UP, 5f), // warm up
                    RunSegment.Time(RunSegmentSpeed.RUN, 10f),
                    RunSegment.Time(RunSegmentSpeed.WALK, 3f),
                    RunSegment.Time(RunSegmentSpeed.RUN, 10f),
                    RunSegment.Time(RunSegmentSpeed.COOL_DOWN, 5f), // cool down
                )
                "w6d3" -> arrayListOf(
                    RunSegment.Time(RunSegmentSpeed.WARM_UP, 5f), // warm up
                    RunSegment.Time(RunSegmentSpeed.RUN, 25f),
                    RunSegment.Time(RunSegmentSpeed.COOL_DOWN, 5f), // cool down
                )
                "w7d1" -> arrayListOf(
                    RunSegment.Time(RunSegmentSpeed.WARM_UP, 5f), // warm up
                    RunSegment.Time(RunSegmentSpeed.RUN, 25f),
                    RunSegment.Time(RunSegmentSpeed.COOL_DOWN, 5f), //cool down
                )
                "w7d2" -> arrayListOf(
                    RunSegment.Time(RunSegmentSpeed.WARM_UP, 5f), // warm up
                    RunSegment.Time(RunSegmentSpeed.RUN, 25f),
                    RunSegment.Time(RunSegmentSpeed.COOL_DOWN, 5f), // cool down
                )
                "w7d3" -> arrayListOf(
                    RunSegment.Time(RunSegmentSpeed.WARM_UP, 5f), // warm up
                    RunSegment.Time(RunSegmentSpeed.RUN, 25f),
                    RunSegment.Time(RunSegmentSpeed.COOL_DOWN, 5f), // cool down
                )
                "w8d1" -> arrayListOf(
                    RunSegment.Time(RunSegmentSpeed.WARM_UP, 5f), // warm up
                    RunSegment.Time(RunSegmentSpeed.RUN, 30f),
                    RunSegment.Time(RunSegmentSpeed.COOL_DOWN, 5f), //cool down
                )
                "w8d2" -> arrayListOf(
                    RunSegment.Time(RunSegmentSpeed.WARM_UP, 5f), // warm up
                    RunSegment.Time(RunSegmentSpeed.RUN, 30f),
                    RunSegment.Time(RunSegmentSpeed.COOL_DOWN, 5f), // cool down
                )
                "w8d3" -> arrayListOf(
                    RunSegment.Time(RunSegmentSpeed.WARM_UP, 5f), // warm up
                    RunSegment.Time(RunSegmentSpeed.RUN, 30f),
                    RunSegment.Time(RunSegmentSpeed.COOL_DOWN, 5f), // cool down
                )
                else -> throw java.lang.Exception("Could not find run")
            }
        }
    }

    override fun getRunSegments(): ArrayList<RunSegment> { return _getRunSegments(Code) }
}
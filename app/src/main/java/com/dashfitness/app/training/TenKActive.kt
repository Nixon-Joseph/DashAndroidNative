package com.dashfitness.app.training

import com.dashfitness.app.ui.main.run.models.RunSegment
import com.dashfitness.app.ui.main.run.models.RunSegmentSpeed
import com.dashfitness.app.util.milesToKilometers
import java.io.Serializable

// Shamelessly pulled from https://marathonhandbook.com
class TenKActive: TrainingPlan(
    CODE,
    NAME,
    DESCRIPTION,
    arrayListOf(
        TenKActiveTrainingRun("w1d1", "Week 1 Day 1", "Lorem Ipsum"),
        TenKActiveTrainingRun("w1d2", "Week 1 Day 2", "Lorem Ipsum"),
        TenKActiveTrainingRun("w1d3", "Week 1 Day 3", "Lorem Ipsum"),
        TenKActiveTrainingRun("w2d1", "Week 2 Day 1", "Lorem Ipsum"),
        TenKActiveTrainingRun("w2d2", "Week 2 Day 2", "Lorem Ipsum"),
        TenKActiveTrainingRun("w2d3", "Week 2 Day 3", "Lorem Ipsum"),
        TenKActiveTrainingRun("w3d1", "Week 3 Day 1", "Lorem Ipsum"),
        TenKActiveTrainingRun("w3d2", "Week 3 Day 2", "Lorem Ipsum"),
        TenKActiveTrainingRun("w3d3", "Week 3 Day 3", "Lorem Ipsum"),
        TenKActiveTrainingRun("w4d1", "Week 4 Day 1", "Lorem Ipsum"),
        TenKActiveTrainingRun("w4d2", "Week 4 Day 2", "Lorem Ipsum"),
        TenKActiveTrainingRun("w4d3", "Week 4 Day 3", "Lorem Ipsum"),
        TenKActiveTrainingRun("w5d1", "Week 5 Day 1", "Lorem Ipsum"),
        TenKActiveTrainingRun("w5d2", "Week 5 Day 2", "Lorem Ipsum"),
        TenKActiveTrainingRun("w5d3", "Week 5 Day 3", "Lorem Ipsum"),
        TenKActiveTrainingRun("w5d4", "Week 5 Day 4", "Lorem Ipsum"),
        TenKActiveTrainingRun("w6d1", "Week 6 Day 1", "Lorem Ipsum"),
        TenKActiveTrainingRun("w6d2", "Week 6 Day 2", "Lorem Ipsum"),
        TenKActiveTrainingRun("w6d3", "Week 6 Day 3", "Lorem Ipsum"),
        TenKActiveTrainingRun("w6d4", "Week 6 Day 4", "Lorem Ipsum"),
        TenKActiveTrainingRun("w7d1", "Week 7 Day 1", "Lorem Ipsum"),
        TenKActiveTrainingRun("w7d2", "Week 7 Day 2", "Lorem Ipsum"),
        TenKActiveTrainingRun("w7d3", "Week 7 Day 3", "Lorem Ipsum"),
        TenKActiveTrainingRun("w7d4", "Week 7 Day 4", "Lorem Ipsum"),
        TenKActiveTrainingRun("w8d1", "Week 8 Day 1", "Lorem Ipsum"),
        TenKActiveTrainingRun("w8d2", "Week 8 Day 2", "Lorem Ipsum"),
        TenKActiveTrainingRun("w8d3", "Week 8 Day 3", "Lorem Ipsum"),
        TenKActiveTrainingRun("w8d4", "Week 8 Day 4", "Lorem Ipsum"),
    )
), Serializable {
    companion object {
        const val CODE: String = "10ka"
        const val NAME: String = "10K Active"
        const val DESCRIPTION: String = "Lorem Ipsum"
    }
}

class TenKActiveTrainingRun(
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
                "w4d2" -> arrayListOf(
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
                "w5d1" -> arrayListOf(
                    RunSegment.Time(RunSegmentSpeed.WARM_UP, 5f), // warm up
                    RunSegment.Time(RunSegmentSpeed.RUN, milesToKilometers(2.5f)),
                    RunSegment.Time(RunSegmentSpeed.COOL_DOWN, 5f), //cool down
                )
                "w5d2" -> arrayListOf(
                    RunSegment.Time(RunSegmentSpeed.WARM_UP, 5f), // warm up
                    RunSegment.Time(RunSegmentSpeed.RUN, milesToKilometers(1.5f)),
                    RunSegment.Time(RunSegmentSpeed.COOL_DOWN, 5f), // cool down
                )
                "w5d3" -> arrayListOf(
                    RunSegment.Time(RunSegmentSpeed.WARM_UP, 5f), // warm up
                    RunSegment.Time(RunSegmentSpeed.RUN, milesToKilometers(2.5f)),
                    RunSegment.Time(RunSegmentSpeed.COOL_DOWN, 5f), // cool down
                )
                "w5d4" -> arrayListOf(
                    RunSegment.Time(RunSegmentSpeed.WARM_UP, 5f), // warm up
                    RunSegment.Time(RunSegmentSpeed.RUN, milesToKilometers(4f)),
                    RunSegment.Time(RunSegmentSpeed.COOL_DOWN, 5f), // cool down
                )
                "w6d1" -> arrayListOf(
                    RunSegment.Time(RunSegmentSpeed.WARM_UP, 5f), // warm up
                    RunSegment.Time(RunSegmentSpeed.RUN, milesToKilometers(2.5f)),
                    RunSegment.Time(RunSegmentSpeed.COOL_DOWN, 5f), //cool down
                )
                "w6d2" -> arrayListOf(
                    RunSegment.Time(RunSegmentSpeed.WARM_UP, 5f), // warm up
                    RunSegment.Time(RunSegmentSpeed.RUN, milesToKilometers(1.5f)),
                    RunSegment.Time(RunSegmentSpeed.COOL_DOWN, 5f), // cool down
                )
                "w6d3" -> arrayListOf(
                    RunSegment.Time(RunSegmentSpeed.WARM_UP, 5f), // warm up
                    RunSegment.Time(RunSegmentSpeed.RUN, milesToKilometers(2.5f)),
                    RunSegment.Time(RunSegmentSpeed.COOL_DOWN, 5f), //cool down
                )
                "w6d4" -> arrayListOf(
                    RunSegment.Time(RunSegmentSpeed.WARM_UP, 5f), // warm up
                    RunSegment.Time(RunSegmentSpeed.RUN, milesToKilometers(4.5f)),
                    RunSegment.Time(RunSegmentSpeed.COOL_DOWN, 5f), // cool down
                )
                "w7d1" -> arrayListOf(
                    RunSegment.Time(RunSegmentSpeed.WARM_UP, 5f), // warm up
                    RunSegment.Time(RunSegmentSpeed.RUN, milesToKilometers(3f)),
                    RunSegment.Time(RunSegmentSpeed.COOL_DOWN, 5f), //cool down
                )
                "w7d2" -> arrayListOf(
                    RunSegment.Time(RunSegmentSpeed.WARM_UP, 5f), // warm up
                    RunSegment.Time(RunSegmentSpeed.RUN, milesToKilometers(4f)),
                    RunSegment.Time(RunSegmentSpeed.COOL_DOWN, 5f), // cool down
                )
                "w7d3" -> arrayListOf(
                    RunSegment.Time(RunSegmentSpeed.WARM_UP, 5f), // warm up
                    RunSegment.Time(RunSegmentSpeed.RUN, milesToKilometers(3f)),
                    RunSegment.Time(RunSegmentSpeed.COOL_DOWN, 5f), // cool down
                )
                "w7d4" -> arrayListOf(
                    RunSegment.Time(RunSegmentSpeed.WARM_UP, 5f), // warm up
                    RunSegment.Time(RunSegmentSpeed.RUN, milesToKilometers(5f)),
                    RunSegment.Time(RunSegmentSpeed.COOL_DOWN, 5f), // cool down
                )
                "w8d1" -> arrayListOf(
                    RunSegment.Time(RunSegmentSpeed.WARM_UP, 5f), // warm up
                    RunSegment.Time(RunSegmentSpeed.RUN, milesToKilometers(3f)),
                    RunSegment.Time(RunSegmentSpeed.COOL_DOWN, 5f), //cool down
                )
                "w8d2" -> arrayListOf(
                    RunSegment.Time(RunSegmentSpeed.WARM_UP, 5f), // warm up
                    RunSegment.Time(RunSegmentSpeed.RUN, milesToKilometers(4f)),
                    RunSegment.Time(RunSegmentSpeed.COOL_DOWN, 5f), // cool down
                )
                "w8d3" -> arrayListOf(
                    RunSegment.Time(RunSegmentSpeed.WARM_UP, 5f), // warm up
                    RunSegment.Time(RunSegmentSpeed.RUN, milesToKilometers(3f)),
                    RunSegment.Time(RunSegmentSpeed.COOL_DOWN, 5f), // cool down
                )
                "w8d4" -> arrayListOf(
                    RunSegment.Time(RunSegmentSpeed.WARM_UP, 5f), // warm up
                    RunSegment.Time(RunSegmentSpeed.RUN, 10f), // 10K
                    RunSegment.Time(RunSegmentSpeed.COOL_DOWN, 5f), // cool down
                )
                else -> throw java.lang.Exception("Could not find run")
            }
        }
    }

    override fun getRunSegments(): ArrayList<RunSegment> { return _getRunSegments(Code) }
}
package com.dashfitness.app.training

import com.dashfitness.app.ui.main.run.models.RunSegment
import com.dashfitness.app.ui.main.run.models.RunSegmentSpeed
import com.dashfitness.app.util.milesToKilometers
import java.io.Serializable

// Shamelessly pulled from https://marathonhandbook.com
class TenKBeginner: TrainingPlan(
    CODE,
    NAME,
    DESCRIPTION,
    arrayListOf(
        TenKTrainingRun("w1d1", "Week 1 Day 1", "1 min running / 1.5 min walking, for 20 minutes."),
        TenKTrainingRun("w1d2", "Week 1 Day 2", "1 min running / 1.5 min walking, for 20 minutes."),
        TenKTrainingRun("w1d3", "Week 1 Day 3", "1 min running / 1.5 min walking, for 20 minutes."),
        TenKTrainingRun("w2d1", "Week 2 Day 1", "1.5 min running / 2 min walking, for 20 minutes."),
        TenKTrainingRun("w2d2", "Week 2 Day 2", "1.5 min running / 2 min walking, for 20 minutes."),
        TenKTrainingRun("w2d3", "Week 2 Day 3", "1.5 min running / 2 min walking, for 20 minutes."),
        TenKTrainingRun("w3d1", "Week 3 Day 1", "2 min running / 2 min walking, for 20 minutes."),
        TenKTrainingRun("w3d2", "Week 3 Day 2", "2.5 min running / 2.5 min walking, for 20 minutes."),
        TenKTrainingRun("w3d3", "Week 3 Day 3", "2.5 min running / 2.5 min walking, for 20 minutes."),
        TenKTrainingRun("w4d1", "Week 4 Day 1", "3 min running / 2 min walking, for 20 minutes."),
        TenKTrainingRun("w4d2", "Week 4 Day 2", "3 min running / 2 min walking, for 20 minutes."),
        TenKTrainingRun("w4d3", "Week 4 Day 3", "3 min running / 2 min walking, for 20 minutes."),
        TenKTrainingRun("w5d1", "Week 5 Day 1", "5 min running / 3 min walking (x3)."),
        TenKTrainingRun("w5d2", "Week 5 Day 2", "8 min running / 5 min walking / 8 min running."),
        TenKTrainingRun("w5d3", "Week 5 Day 3", "20 min running."),
        TenKTrainingRun("w6d1", "Week 6 Day 1", "6 min running / 3 min walking (x2)."),
        TenKTrainingRun("w6d2", "Week 6 Day 2", "10 min running / 3 min walking / 10 min running."),
        TenKTrainingRun("w6d3", "Week 6 Day 3", "25 min running."),
        TenKTrainingRun("w7d1", "Week 7 Day 1", "24 min running."),
        TenKTrainingRun("w7d2", "Week 7 Day 2", "24 min running."),
        TenKTrainingRun("w7d3", "Week 7 Day 3", "24 min running."),
        TenKTrainingRun("w8d1", "Week 8 Day 1", "30 min running."),
        TenKTrainingRun("w8d2", "Week 8 Day 2", "30 min running."),
        TenKTrainingRun("w8d3", "Week 8 Day 3", "30 min running."),
        TenKTrainingRun("w8d4", "Week 8 Day 4", "Run a 5K!"),
        TenKTrainingRun("w9d1", "Week 9 Day 1", "2.5 mile run."),
        TenKTrainingRun("w9d2", "Week 9 Day 2", "1.5 mile run."),
        TenKTrainingRun("w9d3", "Week 9 Day 3", "2.5 mile run."),
        TenKTrainingRun("w9d4", "Week 9 Day 4", "4 mile run."),
        TenKTrainingRun("w10d1", "Week 10 Day 1", "2.5 mile run."),
        TenKTrainingRun("w10d2", "Week 10 Day 2", "1.5 mile run."),
        TenKTrainingRun("w10d3", "Week 10 Day 3", "2.5 mile run."),
        TenKTrainingRun("w10d4", "Week 10 Day 4", "4.5 mile run."),
        TenKTrainingRun("w11d1", "Week 11 Day 1", "3 mile run."),
        TenKTrainingRun("w11d2", "Week 11 Day 2", "4 mile run."),
        TenKTrainingRun("w11d3", "Week 11 Day 3", "3 mile run."),
        TenKTrainingRun("w11d4", "Week 11 Day 4", "5 mile run."),
        TenKTrainingRun("w12d1", "Week 12 Day 1", "3 mile run."),
        TenKTrainingRun("w12d2", "Week 12 Day 2", "4 mile run."),
        TenKTrainingRun("w12d3", "Week 12 Day 3", "3 mile run."),
        TenKTrainingRun("w12d4", "Week 12 Day 4", "Run your 10K!"),
    )
), Serializable {
    companion object {
        const val CODE: String = "10kb"
        const val NAME: String = "10K Beginner"
        const val DESCRIPTION: String = "The 10k distance – around 6.2 miles – is a classic benchmark for both beginners and experienced runners.\n\n" +
                "A solid 10k can be an ambitious goal to aim for, a comfortable training run, or a gruelling race distance to runners.\n\n" +
                "This plan is for beginners or those previously non-active.\n\n" +
                "If you're athletic or more experienced, you might want to consider the \"5K Active\" run plan.\n\n" +
                "Credit: https://marathonhandbook.com - review for more information"
    }
}

class TenKTrainingRun(
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
                "w8d4" -> arrayListOf(
                    RunSegment.Time(RunSegmentSpeed.WARM_UP, 5f), // warm up
                    RunSegment.Distance(RunSegmentSpeed.RUN, 5f), // 5K
                    RunSegment.Time(RunSegmentSpeed.COOL_DOWN, 5f), // cool down
                )
                "w9d1" -> arrayListOf(
                    RunSegment.Time(RunSegmentSpeed.WARM_UP, 5f), // warm up
                    RunSegment.Time(RunSegmentSpeed.RUN, milesToKilometers(2.5f)),
                    RunSegment.Time(RunSegmentSpeed.COOL_DOWN, 5f), //cool down
                )
                "w9d2" -> arrayListOf(
                    RunSegment.Time(RunSegmentSpeed.WARM_UP, 5f), // warm up
                    RunSegment.Time(RunSegmentSpeed.RUN, milesToKilometers(1.5f)),
                    RunSegment.Time(RunSegmentSpeed.COOL_DOWN, 5f), // cool down
                )
                "w9d3" -> arrayListOf(
                    RunSegment.Time(RunSegmentSpeed.WARM_UP, 5f), // warm up
                    RunSegment.Time(RunSegmentSpeed.RUN, milesToKilometers(2.5f)),
                    RunSegment.Time(RunSegmentSpeed.COOL_DOWN, 5f), // cool down
                )
                "w9d4" -> arrayListOf(
                    RunSegment.Time(RunSegmentSpeed.WARM_UP, 5f), // warm up
                    RunSegment.Time(RunSegmentSpeed.RUN, milesToKilometers(4f)),
                    RunSegment.Time(RunSegmentSpeed.COOL_DOWN, 5f), // cool down
                )
                "w10d1" -> arrayListOf(
                    RunSegment.Time(RunSegmentSpeed.WARM_UP, 5f), // warm up
                    RunSegment.Time(RunSegmentSpeed.RUN, milesToKilometers(2.5f)),
                    RunSegment.Time(RunSegmentSpeed.COOL_DOWN, 5f), //cool down
                )
                "w10d2" -> arrayListOf(
                    RunSegment.Time(RunSegmentSpeed.WARM_UP, 5f), // warm up
                    RunSegment.Time(RunSegmentSpeed.RUN, milesToKilometers(1.5f)),
                    RunSegment.Time(RunSegmentSpeed.COOL_DOWN, 5f), // cool down
                )
                "w10d3" -> arrayListOf(
                    RunSegment.Time(RunSegmentSpeed.WARM_UP, 5f), // warm up
                    RunSegment.Time(RunSegmentSpeed.RUN, milesToKilometers(2.5f)),
                    RunSegment.Time(RunSegmentSpeed.COOL_DOWN, 5f), //cool down
                )
                "w10d4" -> arrayListOf(
                    RunSegment.Time(RunSegmentSpeed.WARM_UP, 5f), // warm up
                    RunSegment.Time(RunSegmentSpeed.RUN, milesToKilometers(4.5f)),
                    RunSegment.Time(RunSegmentSpeed.COOL_DOWN, 5f), // cool down
                )
                "w11d1" -> arrayListOf(
                    RunSegment.Time(RunSegmentSpeed.WARM_UP, 5f), // warm up
                    RunSegment.Time(RunSegmentSpeed.RUN, milesToKilometers(3f)),
                    RunSegment.Time(RunSegmentSpeed.COOL_DOWN, 5f), //cool down
                )
                "w11d2" -> arrayListOf(
                    RunSegment.Time(RunSegmentSpeed.WARM_UP, 5f), // warm up
                    RunSegment.Time(RunSegmentSpeed.RUN, milesToKilometers(4f)),
                    RunSegment.Time(RunSegmentSpeed.COOL_DOWN, 5f), // cool down
                )
                "w11d3" -> arrayListOf(
                    RunSegment.Time(RunSegmentSpeed.WARM_UP, 5f), // warm up
                    RunSegment.Time(RunSegmentSpeed.RUN, milesToKilometers(3f)),
                    RunSegment.Time(RunSegmentSpeed.COOL_DOWN, 5f), // cool down
                )
                "w11d4" -> arrayListOf(
                    RunSegment.Time(RunSegmentSpeed.WARM_UP, 5f), // warm up
                    RunSegment.Time(RunSegmentSpeed.RUN, milesToKilometers(5f)),
                    RunSegment.Time(RunSegmentSpeed.COOL_DOWN, 5f), // cool down
                )
                "w12d1" -> arrayListOf(
                    RunSegment.Time(RunSegmentSpeed.WARM_UP, 5f), // warm up
                    RunSegment.Time(RunSegmentSpeed.RUN, milesToKilometers(3f)),
                    RunSegment.Time(RunSegmentSpeed.COOL_DOWN, 5f), //cool down
                )
                "w12d2" -> arrayListOf(
                    RunSegment.Time(RunSegmentSpeed.WARM_UP, 5f), // warm up
                    RunSegment.Time(RunSegmentSpeed.RUN, milesToKilometers(4f)),
                    RunSegment.Time(RunSegmentSpeed.COOL_DOWN, 5f), // cool down
                )
                "w12d3" -> arrayListOf(
                    RunSegment.Time(RunSegmentSpeed.WARM_UP, 5f), // warm up
                    RunSegment.Time(RunSegmentSpeed.RUN, milesToKilometers(3f)),
                    RunSegment.Time(RunSegmentSpeed.COOL_DOWN, 5f), // cool down
                )
                "w12d4" -> arrayListOf(
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
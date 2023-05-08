package com.dashfitness.app.ui.main.run.models

import java.util.*
import java.util.UUID.randomUUID

open class RunSegment(val type: RunSegmentType, val speed: RunSegmentSpeed, val value: Float, val text: String? = null, val isCustomText: Boolean = false): java.io.Serializable {
    companion object {
        fun Time(speed: RunSegmentSpeed, value: Float): RunSegment { return RunSegment(RunSegmentType.TIME, speed, value) }
        fun Distance(speed: RunSegmentSpeed, value: Float): RunSegment { return RunSegment(RunSegmentType.DISTANCE, speed, value) }
    }

    val id: UUID = randomUUID()
}

enum class RunSegmentType {
    TIME, DISTANCE, NONE, ALERT
}

enum class RunSegmentSpeed {
    WALK, RUN, NONE, WARM_UP, COOL_DOWN, TEMPO_RUN
}
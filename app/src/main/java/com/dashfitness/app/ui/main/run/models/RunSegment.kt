package com.dashfitness.app.ui.main.run.models

import java.util.*
import java.util.UUID.randomUUID

open class RunSegment(val type: RunSegmentType, val speed: RunSegmentSpeed, val value: Float): java.io.Serializable {
    val id: UUID = randomUUID()
}

enum class RunSegmentType {
    TIME, DISTANCE, NONE
}

enum class RunSegmentSpeed {
    Walk, Run, NONE
}
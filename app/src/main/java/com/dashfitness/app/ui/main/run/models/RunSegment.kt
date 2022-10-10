package com.dashfitness.app.ui.main.run.models

import java.util.*
import java.util.UUID.randomUUID

class RunSegment(val type: RunSegmentType, val speed: RunSegmentSpeed, val value: Float): java.io.Serializable {
    val id: UUID = randomUUID()
}

enum class RunSegmentType {
    TIME, DISTANCE
}

enum class RunSegmentSpeed {
    Walk, Run
}
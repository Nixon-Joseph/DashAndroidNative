package com.dashfitness.app.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dashfitness.app.ui.main.run.models.RunSegmentSpeed
import com.dashfitness.app.ui.main.run.models.RunSegmentType

@Entity(tableName = "dash_run_segment_data")
class RunSegmentData (
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    @ColumnInfo(name = "run_id")
    val runId: Long,
    @ColumnInfo(name = "start_time_milli")
    val startTimeMilli: Long,
    @ColumnInfo(name = "end_time_milli")
    var endTimeMilli: Long,
    @ColumnInfo(name = "segment_speed")
    var runSpeed: RunSegmentSpeed,
    @ColumnInfo(name = "pace")
    var pace: Long,
    @ColumnInfo(name = "distance")
    var distance: Double
)
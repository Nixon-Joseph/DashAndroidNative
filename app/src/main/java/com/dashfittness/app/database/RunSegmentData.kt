package com.dashfittness.app.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dash_run_segment_data")
class RunSegmentData (
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    @ColumnInfo(name = "run_id")
    val runId: Long,
    @ColumnInfo(name = "start_time_milli")
    val startTimeMilli: Long = android.os.SystemClock.elapsedRealtime(),
    @ColumnInfo(name = "end_time_milli")
    var endTimeMilli: Long = startTimeMilli
)
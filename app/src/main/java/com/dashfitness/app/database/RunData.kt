package com.dashfitness.app.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dash_run_data")
class RunData(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    @ColumnInfo(name = "start_time_milli")
    val startTimeMilli: Long,
    @ColumnInfo(name = "end_time_milli")
    val endTimeMilli: Long,
    @ColumnInfo(name = "total_distance")
    val totalDistance: Double,
    @ColumnInfo(name = "average_pace")
    val averagePace: Long
)
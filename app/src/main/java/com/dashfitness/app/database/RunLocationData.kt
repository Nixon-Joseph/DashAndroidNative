package com.dashfitness.app.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dash_run_location_data")
class RunLocationData(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    @ColumnInfo(name = "run_id")
    val runId: Long,
    @ColumnInfo(name = "latitude")
    val latitude: Double,
    @ColumnInfo(name = "longitude")
    val longitude: Double,
    @ColumnInfo(name = "index")
    val index: Int,
    @ColumnInfo(name = "polyline_index")
    val polylineIndex: Int,
    @ColumnInfo(name = "altitude")
    val altitude: Double
)

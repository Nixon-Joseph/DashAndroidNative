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
    val averagePace: Long,
    @ColumnInfo(name = "calories", defaultValue = "0")
    val calories: Int,
    @ColumnInfo(name = "title", defaultValue = "Run")
    val title: String,
    @ColumnInfo(name = "plan_run_code", defaultValue = "")
    val planRunCode: String,
    @ColumnInfo(name = "plan_run_finished", defaultValue = "0")
    val planRunFinished: Boolean
) {
    override operator fun equals(
        other: Any?
    ): Boolean {
        other?.let {
            return hashCode() == it.hashCode()
        }
        return false
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + startTimeMilli.hashCode()
        result = 31 * result + endTimeMilli.hashCode()
        result = 31 * result + totalDistance.hashCode()
        result = 31 * result + averagePace.hashCode()
        result = 31 * result + calories
        result = 31 * result + title.hashCode()
        result = 31 * result + planRunCode.hashCode()
        result = 31 * result + planRunFinished.hashCode()
        return result
    }
}
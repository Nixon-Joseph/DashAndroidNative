package com.dashfittness.app.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RunDatabaseDao {
    @Insert()
    fun insert(run: RunData): Long

    @Insert()
    fun insert(segment: RunSegmentData) : Long

    @Insert()
    fun insert(locations: List<RunLocationData>)

    @Query("SELECT * FROM dash_run_data ORDER BY start_time_milli")
    fun getAllRuns() : LiveData<List<RunData>>

    @Query("SELECT * FROM dash_run_segment_data WHERE run_id = :runId ORDER BY start_time_milli")
    fun getRunSegments(runId: Long): LiveData<List<RunSegmentData>>

    @Query("SELECT * FROM dash_run_location_data WHERE segment_id = :segmentId ORDER BY `index`")
    fun getSegmentLocations(segmentId: Long): List<RunLocationData>
}
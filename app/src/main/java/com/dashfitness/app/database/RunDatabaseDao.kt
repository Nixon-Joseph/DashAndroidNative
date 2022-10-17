package com.dashfitness.app.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RunDatabaseDao {
    @Insert
    suspend fun insert(run: RunData): Long

    @Insert
    suspend fun insert(segment: RunSegmentData) : Long

    @Insert
    suspend fun insert(locations: List<RunLocationData>)

    @Delete
    suspend fun delete(run: RunData): Int

    @Delete
    suspend fun delete(segment: RunSegmentData): Int

    @Delete
    suspend fun delete(locations: List<RunLocationData>): Int

    @Query("SELECT * FROM dash_run_data ORDER BY start_time_milli DESC")
    fun getAllRuns() : LiveData<List<RunData>>

    @Query("SELECT * FROM dash_run_segment_data WHERE run_id = :runId ORDER BY start_time_milli DESC")
    fun getRunSegments(runId: Long): LiveData<List<RunSegmentData>>

    @Query("SELECT * FROM dash_run_location_data WHERE run_id = :runId ORDER BY segment_id, `index`")
    fun getRunLocations(runId: Long): LiveData<List<RunLocationData>>

    @Query("SELECT * FROM dash_run_location_data WHERE segment_id = :segmentId ORDER BY `index`")
    fun getSegmentLocations(segmentId: Long): List<RunLocationData>
}
package com.dashfitness.app.repositories

import com.dashfitness.app.database.RunData
import com.dashfitness.app.database.RunDatabaseDao
import com.dashfitness.app.database.RunLocationData
import com.dashfitness.app.database.RunSegmentData
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val runDao: RunDatabaseDao
) {
    suspend fun insert(run: RunData) = runDao.insert(run)
    suspend fun insert(segment: RunSegmentData) = runDao.insert(segment)
    suspend fun insert(locations: List<RunLocationData>) = runDao.insert(locations)
    suspend fun delete(run: RunData) = runDao.delete(run)
    suspend fun delete(segment: RunSegmentData) = runDao.delete(segment)
    suspend fun delete(locations: List<RunLocationData>) = runDao.delete(locations)

    fun getAllRuns() = runDao.getAllRuns()
    fun getRunSegments(runId: Long) = runDao.getRunSegments(runId)
    fun getRunLocations(runId: Long) = runDao.getRunLocations(runId)
    fun getSegmentLocations(segmentId: Long) = runDao.getSegmentLocations(segmentId)
}
package com.dashfitness.app.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [RunData::class, RunSegmentData::class, RunLocationData::class], version = 2, exportSchema = false)
abstract class RunDatabase : RoomDatabase() {
    abstract fun getRunDatabaseDao(): RunDatabaseDao
}
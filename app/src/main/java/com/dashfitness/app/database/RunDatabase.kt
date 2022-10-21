package com.dashfitness.app.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        RunData::class,
        RunSegmentData::class,
        RunLocationData::class
               ],
    version = 4,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 3, to = 4)
    ]
)
abstract class RunDatabase : RoomDatabase() {
    abstract fun getRunDatabaseDao(): RunDatabaseDao
}
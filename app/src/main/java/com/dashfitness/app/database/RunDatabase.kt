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
    version = 5,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 3, to = 4),
        AutoMigration(from = 4, to = 5),
    ]
)
abstract class RunDatabase : RoomDatabase() {
    abstract fun getRunDatabaseDao(): RunDatabaseDao
}
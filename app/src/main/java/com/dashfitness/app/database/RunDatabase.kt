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
    version = 7,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 3, to = 4),
        AutoMigration(from = 4, to = 5),
        AutoMigration(from = 5, to = 6),
        AutoMigration(from = 6, to = 7),
    ]
)
abstract class RunDatabase : RoomDatabase() {
    abstract fun getRunDatabaseDao(): RunDatabaseDao
}
package com.dashfittness.app.database
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [RunData::class, RunSegmentData::class, RunLocationData::class], version = 1, exportSchema = false)
abstract class RunDatabase : RoomDatabase() {
    abstract val runDatabaseDao: RunDatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: RunDatabase? = null;

        fun getInstance(context: Context): RunDatabase {
            synchronized(this) {
                var instance = INSTANCE;
                if (instance == null) {
                    instance = Room.databaseBuilder(context.applicationContext, RunDatabase::class.java, "run_database").fallbackToDestructiveMigration().build()
                    INSTANCE = instance;
                }
                return instance;
            }
        }
    }
}
package com.dashfitness.app.di

import android.content.Context
import androidx.room.Room
import com.dashfitness.app.database.RunDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideRunDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        RunDatabase::class.java,
        "run_database"
    ).fallbackToDestructiveMigrationFrom(
        2
    ).build()

    @Provides
    @Singleton
    fun provideRunDatabaseDao(db: RunDatabase) = db.getRunDatabaseDao()
}
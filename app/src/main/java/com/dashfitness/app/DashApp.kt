package com.dashfitness.app

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class DashApp : Application() {
    companion object {
        fun getAppContext(): Context {
            return AppContextHolder.applicationContext
        }

        fun getString(id: Int, vararg formatArgs: Any): String {
            return getAppContext().getString(id, *formatArgs)
        }
    }

    override fun onCreate() {
        super.onCreate()
        AppContextHolder.init(applicationContext)
        Timber.plant(Timber.DebugTree())
    }
}
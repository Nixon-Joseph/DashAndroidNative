package com.dashfitness.app

import android.content.Context

class AppContextHolder private constructor(context: Context) {
    val appContext: Context = context.applicationContext

    companion object {
        private var instance: AppContextHolder? = null

        fun init(context: Context) {
            if (instance == null) {
                instance = AppContextHolder(context)
            }
        }

        val applicationContext: Context by lazy {
            instance?.appContext
                ?: throw IllegalStateException("AppContextHolder is not initialized")
        }
    }
}

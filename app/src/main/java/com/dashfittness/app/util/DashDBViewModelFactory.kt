package com.dashfittness.app.util

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dashfittness.app.RunViewModel
import com.dashfittness.app.database.RunDatabaseDao
import com.dashfittness.app.ui.main.home.HomeViewModel
import com.dashfittness.app.ui.main.run.detail.RunDetailViewModel
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class DashDBViewModelFactory<T>(private val dataSource: RunDatabaseDao, private val application: Application, private val extraParam: T? = null) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(RunViewModel::class.java) -> {
                RunViewModel(dataSource)
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(dataSource, application)
            }
            modelClass.isAssignableFrom(RunDetailViewModel::class.java) -> {
                RunDetailViewModel(dataSource, extraParam!! as Long)
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        } as T
    }
}

open class DBViewModel(protected val database: RunDatabaseDao) : ViewModel()
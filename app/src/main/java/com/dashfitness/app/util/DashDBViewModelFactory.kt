package com.dashfitness.app.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dashfitness.app.RunViewModel
import com.dashfitness.app.database.RunDatabaseDao
import com.dashfitness.app.ui.main.home.HomeViewModel
import com.dashfitness.app.ui.main.run.detail.RunDetailViewModel
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class DashDBViewModelFactory<T>(
    private val dataSource: RunDatabaseDao,
    private val extraParam: T? = null
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(RunViewModel::class.java) -> {
                RunViewModel(dataSource)
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(dataSource)
            }
            modelClass.isAssignableFrom(RunDetailViewModel::class.java) -> {
                RunDetailViewModel(dataSource, extraParam!! as Long)
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        } as T
    }
}

open class DBViewModel(protected val database: RunDatabaseDao) : ViewModel()
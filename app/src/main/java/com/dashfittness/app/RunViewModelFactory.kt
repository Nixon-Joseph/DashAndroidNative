package com.dashfittness.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dashfittness.app.database.RunDatabaseDao
import java.lang.IllegalArgumentException

class RunViewModelFactory(private val dataSource: RunDatabaseDao): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RunViewModel::class.java)) {
            return RunViewModel(dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
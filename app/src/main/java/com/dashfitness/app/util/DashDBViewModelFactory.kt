package com.dashfitness.app.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dashfitness.app.database.RunDatabaseDao
import com.dashfitness.app.ui.main.run.detail.RunDetailViewModel

class RunDetailViewModelFactory(var database: RunDatabaseDao, var runId: Long) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return return RunDetailViewModel(database, runId) as T
    }
}
package com.dashfitness.app.ui.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dashfitness.app.database.RunDatabaseDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    database: RunDatabaseDao
) : ViewModel() {
    /** Coroutine variables */
    /**
     * viewModelJob allows us to cancel all coroutines started by this ViewModel.
     */
    private var viewModelJob = Job()
    /**
     * A [CoroutineScope] keeps track of all coroutines started by this ViewModel.
     *
     * Because we pass it [viewModelJob], any coroutine started in this uiScope can be cancelled
     * by calling `viewModelJob.cancel()`
     *
     * By default, all coroutines started in uiScope will launch in [Dispatchers.Main] which is
     * the main thread on Android. This is a sensible default because most coroutines started by
     * a [ViewModel] update the UI after performing some processing.
     */
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val runs = database.getAllRuns()

    private val _navigateToRunDetail = MutableLiveData<Long?>()
    val navigateToRunDetail: LiveData<Long?>
        get() = _navigateToRunDetail

    fun onRunDataClicked(runId: Long) {
        _navigateToRunDetail.value = runId
    }

    fun afterRunDataClicked() {
        _navigateToRunDetail.value = null
    }
}

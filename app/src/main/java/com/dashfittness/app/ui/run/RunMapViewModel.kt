package com.dashfittness.app.ui.run

import android.view.animation.Transformation
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.dashfittness.app.RunViewModel
import com.dashfittness.app.util.RunClickInterface

class RunMapViewModel : ViewModel(), RunClickInterface {
    private var _runState = MutableLiveData<RunViewModel.RunState>()
    val runState: LiveData<RunViewModel.RunState>
        get() = _runState
    lateinit var clickTarget: RunClickInterface

    fun setRunState(state: RunViewModel.RunState?) {
        _runState.value = state;
    }

    fun onStartRun() {

    }

    override fun onStartClicked() = clickTarget.onStartClicked()
    override fun onPauseClicked()  = clickTarget.onPauseClicked()
    override fun onCancelClicked() = clickTarget.onCancelClicked()
    override fun onEndRunClicked()  = clickTarget.onEndRunClicked()
}

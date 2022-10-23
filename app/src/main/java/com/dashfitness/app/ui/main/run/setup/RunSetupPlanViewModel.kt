package com.dashfitness.app.ui.main.run.setup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dashfitness.app.training.TrainingPlan
import com.dashfitness.app.training.ITrainingRun
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RunSetupPlanViewModel @Inject constructor() : ViewModel() {
    var planName = MutableLiveData<String>()
    val planRuns = MutableLiveData<ArrayList<ITrainingRun>>()

    fun init(plan: TrainingPlan) {
        planName.postValue(plan.Name)
        planRuns.postValue(plan.Runs)
    }

    init {
        planName.postValue("")
        planRuns.postValue(ArrayList())
    }
}
package com.dashfitness.app.ui.main.run.setup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dashfitness.app.training.ITrainingRun
import com.dashfitness.app.training.TrainingPlan
import com.dashfitness.app.util.Event
import com.dashfitness.app.util.EventHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RunSetupPlanViewModel @Inject constructor() : ViewModel() {
    var planDescription = MutableLiveData<String>()
    var planName = MutableLiveData<String>()
    val planRuns = MutableLiveData<ArrayList<ITrainingRun>>()

    fun init(plan: TrainingPlan) {
        planName.postValue(plan.Name)
        planRuns.postValue(plan.Runs)
        planDescription.postValue(plan.Description)
    }

    private val onMoreInforation = EventHandler<Boolean>()
    val moreInformationClick = Event(onMoreInforation)

    fun onMoreInformationClick() {
        onMoreInforation.invoke(true)
    }

    init {
        planName.postValue("")
        planDescription.postValue("")
        planRuns.postValue(ArrayList())
    }
}
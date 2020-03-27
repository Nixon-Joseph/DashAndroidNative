package com.dashfittness.app.ui.main

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RunSetupViewModel : ViewModel() {

    private val _navigateToRunActivity= MutableLiveData<Boolean>()
    val navigateToRunActivity: LiveData<Boolean>
        get() = _navigateToRunActivity;

    fun onRunClick() {
        _navigateToRunActivity.value = true;
    }

    fun onRunNavigated() {
        _navigateToRunActivity.value = false;
    }
}

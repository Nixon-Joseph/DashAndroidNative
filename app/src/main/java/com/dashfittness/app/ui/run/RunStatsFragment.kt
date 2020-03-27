package com.dashfittness.app.ui.run

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.dashfittness.app.RunViewModel
import com.dashfittness.app.databinding.RunStatsFragmentBinding
import com.dashfittness.app.util.RunClickInterface


class RunStatsFragment(private val runClickInterface: RunClickInterface) : Fragment() {
    private lateinit var binding: RunStatsFragmentBinding
    private lateinit var viewModel: RunStatsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = RunStatsFragmentBinding.inflate(inflater)

        viewModel = ViewModelProvider(this).get(RunStatsViewModel::class.java)
        viewModel.clickTarget = runClickInterface
        binding.viewModel = viewModel

        return binding.root
    }

    fun update(state: RunViewModel.RunState?) {
        viewModel.setRunState(state);
    }

}

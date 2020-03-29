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


class RunStatsFragment(runViewModel: RunViewModel) : Fragment() {
    private lateinit var binding: RunStatsFragmentBinding
    private var viewModel: RunViewModel = runViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = RunStatsFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = activity

        binding.viewModel = viewModel

        return binding.root
    }
}

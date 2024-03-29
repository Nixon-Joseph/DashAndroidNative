package com.dashfitness.app.ui.run

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dashfitness.app.ui.viewmodels.RunViewModel
import com.dashfitness.app.databinding.FragmentRunStatsBinding


class RunStatsFragment(runViewModel: RunViewModel) : Fragment() {
    private lateinit var binding: FragmentRunStatsBinding
    private var viewModel: RunViewModel = runViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRunStatsBinding.inflate(inflater)
        binding.lifecycleOwner = activity

        binding.viewModel = viewModel

        return binding.root
    }
}

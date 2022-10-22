package com.dashfitness.app.ui.main.run.setup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dashfitness.app.databinding.FragmentRunSetupTrainingBinding

class RunSetupTrainingFragment(private val viewModel: RunSetupViewModel) : Fragment() {
    private lateinit var binding: FragmentRunSetupTrainingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRunSetupTrainingBinding.inflate(inflater)
        binding.lifecycleOwner = activity
        binding.viewModel = viewModel

        return binding.root
    }
}
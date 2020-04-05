package com.dashfittness.app.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.dashfittness.app.RunActivity
import com.dashfittness.app.databinding.RunSetupFragmentBinding

class RunSetupFragment : Fragment() {
    private lateinit var binding: RunSetupFragmentBinding;
    private lateinit var viewModel: RunSetupViewModel;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = RunSetupFragmentBinding.inflate(inflater);
        return binding.root;
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RunSetupViewModel::class.java)

        viewModel.navigateToRunActivity.observe(viewLifecycleOwner, Observer { navigate ->
            if (navigate) {
                val intent = Intent(activity, RunActivity::class.java)

                startActivity(intent);
                viewModel.onRunNavigated();
            }
        })

        binding.viewModel = viewModel;
    }

}

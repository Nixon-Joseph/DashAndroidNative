package com.dashfittness.app.ui.main.run.setup

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dashfittness.app.R

import com.dashfittness.app.RunActivity
import com.dashfittness.app.databinding.FragmentRunSetupBinding
import com.dashfittness.app.util.animateView

class RunSetupFragment : Fragment() {
    private lateinit var binding: FragmentRunSetupBinding;
    private lateinit var viewModel: RunSetupViewModel;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRunSetupBinding.inflate(inflater);
        return binding.root;
    }

    override fun onResume() {
        super.onResume()
        activity?.findViewById<FrameLayout>(R.id.progress_overlay)?.animateView(View.GONE, 0.0f, 0)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RunSetupViewModel::class.java)

        viewModel.navigateToRunActivity.observe(viewLifecycleOwner, Observer { navigate ->
            if (navigate) {
                activity?.findViewById<FrameLayout>(R.id.progress_overlay)?.animateView(View.VISIBLE, 0.4f, 200)
                val intent = Intent(activity, RunActivity::class.java)

                startActivity(intent);
                viewModel.onRunNavigated();
            }
        })

        binding.viewModel = viewModel;
    }

}

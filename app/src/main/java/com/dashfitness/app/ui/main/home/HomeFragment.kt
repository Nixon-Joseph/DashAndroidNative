package com.dashfitness.app.ui.main.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dashfitness.app.R
import com.dashfitness.app.database.RunDatabase
import com.dashfitness.app.databinding.FragmentHomeBinding
import com.dashfitness.app.util.DashDBViewModelFactory

class HomeFragment : Fragment() {
    private lateinit var viewModel: HomeViewModel
    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding: FragmentHomeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        val application = requireNotNull(this.activity).application

        val dataSource = RunDatabase.getInstance(application).runDatabaseDao
        val viewModelFactory = DashDBViewModelFactory<Int>(dataSource, application)

        val viewModel = ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)

        binding.viewModel = viewModel

        val adapter = RunDataAdapter(RunDataListener { runId ->
            viewModel.onRunDataClicked(runId.id)
        })
        binding.runList.adapter = adapter
        linearLayoutManager = LinearLayoutManager(activity)
        binding.runList.layoutManager = linearLayoutManager

        viewModel.runs.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        viewModel.navigateToRunDetail.observe(viewLifecycleOwner, Observer {
            it?.let {
                val controller = this.findNavController()
                if (controller.currentDestination?.id == R.id.home) {
                    controller.navigate(HomeFragmentDirections.actionHomeToRunDetailFragment(it))
                }
                viewModel.afterRunDataClicked()
            }
        })

        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
    }
}

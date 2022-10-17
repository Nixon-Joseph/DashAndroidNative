package com.dashfitness.app.ui.main.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dashfitness.app.R
import com.dashfitness.app.database.RunDatabaseDao
import com.dashfitness.app.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var linearLayoutManager: LinearLayoutManager
    @Inject
    lateinit var dataSource: RunDatabaseDao

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding: FragmentHomeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

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
}

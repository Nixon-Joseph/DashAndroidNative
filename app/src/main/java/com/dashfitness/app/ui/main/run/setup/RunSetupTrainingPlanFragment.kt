package com.dashfitness.app.ui.main.run.setup

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.dashfitness.app.R
import com.dashfitness.app.databinding.FragmentRunSetupTrainingBinding
import com.dashfitness.app.databinding.FragmentRunSetupTrainingPlanBinding
import com.dashfitness.app.training.*

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PLAN = "plan"

/**
 * Use the [RunSetupTrainingPlanFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RunSetupTrainingPlanFragment : Fragment() {
    lateinit var binding: FragmentRunSetupTrainingPlanBinding
    private val viewModel: RunSetupPlanViewModel by viewModels()
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var plan: TrainingPlan? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            plan = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getSerializable(ARG_PLAN, TrainingPlan::class.java)
            } else {
                it.getSerializable(ARG_PLAN) as TrainingPlan
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRunSetupTrainingPlanBinding.inflate(inflater)
        linearLayoutManager = LinearLayoutManager(requireActivity())
        binding.viewModel = viewModel

        val adapter = TrainingRunAdapter(TrainingRunListener { run ->
            viewModel.onTrainingRunClicked(run)
        })
        binding.trainingRunList.adapter = adapter
        binding.trainingRunList.layoutManager = linearLayoutManager

        viewModel.planRuns.observe(requireActivity(), Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        viewModel.openSelectedTrainingRunInfo.observe(requireActivity()) {
            // TODO: open detail modal with 'start run' button
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    fun onSelectRun(runCode: String) {
        runSelected.postValue(plan?.Runs?.first { it.Code == runCode })
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param plan Training Plan.
         * @return A new instance of fragment RunSetupTrainingPlanFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(plan: TrainingPlans) =
            RunSetupTrainingPlanFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PLAN, getPlan(plan))
                }
            }

        val runSelected = MutableLiveData<ITrainingRun>()

        private fun getPlan(plan: TrainingPlans) : TrainingPlan {
            return when (plan) {
                TrainingPlans.FIVE_K_BEGINNER -> FiveKBeginner()
                TrainingPlans.FIVE_K_ACTIVE -> FiveKActive()
                TrainingPlans.TEN_K_BEGINNER -> TenKBeginner()
                TrainingPlans.TEN_K_ACTIVE -> TenKActive()
            }
        }
    }
}

enum class TrainingPlans(val value: Int) {
    FIVE_K_BEGINNER(1),
    FIVE_K_ACTIVE(2),
    TEN_K_BEGINNER(3),
    TEN_K_ACTIVE(4);
}
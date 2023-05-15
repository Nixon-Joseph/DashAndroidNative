package com.dashfitness.app.ui.main.run.setup

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.asFlow
import androidx.recyclerview.widget.LinearLayoutManager
import com.dashfitness.app.R
import com.dashfitness.app.database.RunDatabaseDao
import com.dashfitness.app.databinding.FragmentRunSetupTrainingPlanBinding
import com.dashfitness.app.training.*
import com.google.android.material.textview.MaterialTextView
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PLAN = "plan"

/**
 * Use the [RunSetupTrainingPlanFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RunSetupTrainingPlanFragment(private val parentViewModel: RunSetupViewModel, private val dataSource: RunDatabaseDao) : Fragment() {
    private val viewModel: RunSetupPlanViewModel by viewModels()
    lateinit var binding: FragmentRunSetupTrainingPlanBinding
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var plan: TrainingPlan

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireArguments().let {
            plan = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getSerializable(ARG_PLAN, TrainingPlan::class.java)!!
            } else {
                it.getSerializable(ARG_PLAN) as TrainingPlan
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRunSetupTrainingPlanBinding.inflate(inflater)
        linearLayoutManager = LinearLayoutManager(requireActivity())
        binding.viewModel = viewModel

        val adapter = TrainingRunAdapter(TrainingRunListener { run ->
            showRunDialog(run, inflater)
        })
        binding.lifecycleOwner = viewLifecycleOwner
        binding.trainingRunList.adapter = adapter
        binding.trainingRunList.layoutManager = linearLayoutManager

        viewModel.planRuns.observe(requireActivity(), Observer {
            it?.let {
                adapter.submitList(it)
                GlobalScope.launch {
                    fillFinishedRunData(it, adapter)
                }
            }
        })

        viewModel.moreInformationClick += {
            showRunSummaryDialog(plan) //, inflater
        }

        plan.let {
            viewModel.init(it)
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    private suspend fun fillFinishedRunData(
        trainingRuns: ArrayList<ITrainingRun>,
        adapter: TrainingRunAdapter
    ) {
        val runs = dataSource.getCompletedRunDataForPlan(plan.Code).asFlow().first()
        runs.let {
            it.forEach {run ->
                trainingRuns.forEach {tRun ->
                    if (run.planRunCode == plan.buildPlanRunCode(tRun.code)) {
                        tRun.finishedRunStartDate = run.startTimeMilli
                    }
                }
            }
            MainScope().launch {
                adapter.submitList(trainingRuns)
            }
        }
    }

    private fun showRunDialog(run: ITrainingRun, inflater: LayoutInflater) {
        val builder = requireActivity().let { AlertDialog.Builder(it) }
        val view = inflater.inflate(R.layout.dialog_select_training_run, null)
        builder
            .setTitle("${plan.Name} - ${run.name}")
            .setView(view)
            .setPositiveButton("Start Run") { _dialog, _ ->
                parentViewModel?.launchRunActivity(run.getRunSegments(), plan.buildPlanRunCode(run.code))
                _dialog.dismiss()
            }
            .setNegativeButton("Cancel") { _dialog, _ -> _dialog.dismiss() }
        view.findViewById<MaterialTextView>(R.id.selectTrainingSummaryTextView).text = run.summary
        val dialog = builder.create()
        dialog.show()
    }

    private fun showRunSummaryDialog(plan: TrainingPlan/*, inflater: LayoutInflater*/) {
        val builder = requireActivity().let { AlertDialog.Builder(it) }
        builder
            .setTitle(plan.Name)
            .setMessage(Html.fromHtml(plan.Description, Html.TO_HTML_PARAGRAPH_LINES_INDIVIDUAL))
            .setPositiveButton("Ok") { _dialog, _ ->
                _dialog.dismiss()
            }
            .setNegativeButton("Dismiss") { _dialog, _ -> _dialog.dismiss() }
        val dialog = builder.create()
        dialog.show()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param plan Training Plan.
         * @return A new instance of fragment RunSetupTrainingPlanFragment.
         */
        fun newInstance(plan: TrainingPlans, viewModel: RunSetupViewModel, dataSource: RunDatabaseDao) =
            RunSetupTrainingPlanFragment(viewModel, dataSource).apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PLAN, getPlan(plan))
                }
            }

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
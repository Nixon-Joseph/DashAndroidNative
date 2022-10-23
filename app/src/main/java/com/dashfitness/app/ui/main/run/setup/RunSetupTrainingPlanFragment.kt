package com.dashfitness.app.ui.main.run.setup

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.dashfitness.app.R
import com.dashfitness.app.RunActivity
import com.dashfitness.app.databinding.FragmentRunSetupTrainingBinding
import com.dashfitness.app.databinding.FragmentRunSetupTrainingPlanBinding
import com.dashfitness.app.training.*
import com.dashfitness.app.ui.main.run.models.RunSegment
import com.dashfitness.app.ui.main.run.models.RunSegmentSpeed
import com.dashfitness.app.ui.main.run.models.RunSegmentType
import com.google.android.material.slider.Slider
import com.google.android.material.textview.MaterialTextView
import java.util.*

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
            showRunDialog(run, inflater)
        })
        binding.lifecycleOwner = viewLifecycleOwner
        binding.trainingRunList.adapter = adapter
        binding.trainingRunList.layoutManager = linearLayoutManager

        viewModel.planRuns.observe(requireActivity(), Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        viewModel.moreInformationClick += {
            showRunSummaryDialog(plan!!) //, inflater
        }

        plan?.let {
            viewModel.init(it)
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun showRunDialog(run: ITrainingRun, inflater: LayoutInflater) {
        val builder = requireActivity().let { AlertDialog.Builder(it) }
        val view = inflater.inflate(R.layout.dialog_select_training_run, null)
        builder
            .setTitle("${plan?.Name} - ${run.Name}")
            .setView(view)
            .setPositiveButton("Start Run") { _dialog, _ ->
                val intent = Intent(activity, RunActivity::class.java)
                intent.putExtra("segments", run.getRunSegments())
                startActivity(intent)
                _dialog.dismiss()
            }
            .setNegativeButton("Cancel") { _dialog, _ -> _dialog.dismiss() }
        view.findViewById<MaterialTextView>(R.id.selectTrainingSummaryTextView).text = run.Summary
        val dialog = builder.create()
        dialog.show()
    }

    private fun showRunSummaryDialog(run: TrainingPlan/*, inflater: LayoutInflater*/) {
        val builder = requireActivity().let { AlertDialog.Builder(it) }
//        val view = inflater.inflate(R.layout.dialog_select_training_run, null)
        builder
            .setTitle("${plan?.Name}")
            .setMessage(Html.fromHtml(plan?.Description, Html.TO_HTML_PARAGRAPH_LINES_INDIVIDUAL))
//            .setView(view)
            .setPositiveButton("marathonhandbook.com") { _dialog, _ ->
                val webpage: Uri = Uri.parse("https://marathonhandbook.com")
                val intent = Intent(Intent.ACTION_VIEW, webpage)
                startActivity(intent)
                _dialog.dismiss()
            }
            .setNegativeButton("Dismiss") { _dialog, _ -> _dialog.dismiss() }
//        view.findViewById<MaterialTextView>(R.id.selectTrainingSummaryTextView).text = run.Summary
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
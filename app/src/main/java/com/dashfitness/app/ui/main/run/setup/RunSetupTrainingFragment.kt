package com.dashfitness.app.ui.main.run.setup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dashfitness.app.databinding.FragmentRunSetupTrainingBinding

class RunSetupTrainingFragment(private val viewModel: RunSetupViewModel) : Fragment() {
    private lateinit var binding: FragmentRunSetupTrainingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRunSetupTrainingBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val adapter = ViewPageAdapter(TrainingPlans.length(), this, viewModel)

        binding.trainingPlanPager.adapter = adapter

        return binding.root
    }

    class ViewPageAdapter(private val numPages: Int, fragment: Fragment, private val viewModel: RunSetupViewModel) : FragmentStateAdapter(fragment) {
        override fun createFragment(position: Int): Fragment {
            return when (position) {
                TrainingPlans.FIVE_K_BEGINNER.value -> RunSetupTrainingPlanFragment.newInstance(TrainingPlans.FIVE_K_BEGINNER, viewModel)
                TrainingPlans.FIVE_K_ACTIVE.value -> RunSetupTrainingPlanFragment.newInstance(TrainingPlans.FIVE_K_ACTIVE, viewModel)
                TrainingPlans.TEN_K_BEGINNER.value -> RunSetupTrainingPlanFragment.newInstance(TrainingPlans.TEN_K_BEGINNER, viewModel)
                TrainingPlans.TEN_K_ACTIVE.value -> RunSetupTrainingPlanFragment.newInstance(TrainingPlans.TEN_K_ACTIVE, viewModel)
                else -> throw Exception("Out of range")
            }
        }

        override fun getItemCount(): Int = numPages
    }
}

/*******
 * The number determines the order in which the views display.
 * NOTE, one one is added, you'll also need to add it to the 'when' statement above.
 * Everything else should be automatic
*******/
enum class TrainingPlans(val value: Int) {
    FIVE_K_BEGINNER(0),
    FIVE_K_ACTIVE(1),
    TEN_K_BEGINNER(2),
    TEN_K_ACTIVE(3);

    companion object {
        fun length() = values().size
    }
}
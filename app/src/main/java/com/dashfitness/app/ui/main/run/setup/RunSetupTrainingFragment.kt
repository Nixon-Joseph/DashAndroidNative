package com.dashfitness.app.ui.main.run.setup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dashfitness.app.databinding.FragmentRunSetupTrainingBinding
import com.dashfitness.app.training.FiveKActive
import com.dashfitness.app.training.FiveKBeginner
import com.dashfitness.app.training.TenKActive
import com.dashfitness.app.training.TenKBeginner
import java.util.ArrayList

class RunSetupTrainingFragment(private val viewModel: RunSetupViewModel) : Fragment() {
    private lateinit var binding: FragmentRunSetupTrainingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRunSetupTrainingBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val adapter = ViewPageAdapter(TrainingPlans.length(), this)

        binding.trainingPlanPager.adapter = adapter

        return binding.root
    }

    class ViewPageAdapter(private val numPages: Int, fragment: Fragment) : FragmentStateAdapter(fragment) {
        override fun createFragment(position: Int): Fragment {
            return when (position) {
                TrainingPlans.FIVE_K_BEGINNER.value -> RunSetupTrainingPlanFragment.newInstance(TrainingPlans.FIVE_K_BEGINNER)
                TrainingPlans.FIVE_K_ACTIVE.value -> RunSetupTrainingPlanFragment.newInstance(TrainingPlans.FIVE_K_ACTIVE)
                TrainingPlans.TEN_K_BEGINNER.value -> RunSetupTrainingPlanFragment.newInstance(TrainingPlans.TEN_K_BEGINNER)
                TrainingPlans.TEN_K_ACTIVE.value -> RunSetupTrainingPlanFragment.newInstance(TrainingPlans.TEN_K_ACTIVE)
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
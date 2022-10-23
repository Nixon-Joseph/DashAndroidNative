package com.dashfitness.app.ui.main.run.setup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.dashfitness.app.databinding.FragmentRunSetupTrainingBinding
import com.dashfitness.app.training.FiveKActive
import com.dashfitness.app.training.FiveKBeginner
import com.dashfitness.app.training.TenKActive
import com.dashfitness.app.training.TenKBeginner
import java.util.ArrayList

class RunSetupTrainingFragment(private val viewModel: RunSetupViewModel) : Fragment() {
    private lateinit var binding: FragmentRunSetupTrainingBinding

    private lateinit var fiveKBeginnerFragment: RunSetupTrainingPlanFragment
    private lateinit var fiveKActiveFragment: RunSetupTrainingPlanFragment
    private lateinit var tenKBeginnerFragment: RunSetupTrainingPlanFragment
    private lateinit var tenKActiveFragment: RunSetupTrainingPlanFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRunSetupTrainingBinding.inflate(inflater)
        binding.lifecycleOwner = activity
        binding.viewModel = viewModel

        fiveKBeginnerFragment = RunSetupTrainingPlanFragment.newInstance(TrainingPlans.FIVE_K_BEGINNER)
        fiveKActiveFragment = RunSetupTrainingPlanFragment.newInstance(TrainingPlans.FIVE_K_ACTIVE)
        tenKBeginnerFragment = RunSetupTrainingPlanFragment.newInstance(TrainingPlans.TEN_K_BEGINNER)
        tenKActiveFragment = RunSetupTrainingPlanFragment.newInstance(TrainingPlans.TEN_K_ACTIVE)

        val adapter = ViewPageAdapter(parentFragmentManager)
        adapter.addFragment(fiveKBeginnerFragment, FiveKBeginner.NAME)
        adapter.addFragment(fiveKActiveFragment, FiveKActive.NAME)
        adapter.addFragment(tenKBeginnerFragment, TenKBeginner.NAME)
        adapter.addFragment(tenKActiveFragment, TenKActive.NAME)

        binding.trainingPlanPager.adapter = adapter

        return binding.root
    }

    class ViewPageAdapter(supportFragmentManager: FragmentManager) : FragmentStatePagerAdapter(supportFragmentManager) {
        private val _fragmentList = ArrayList<Fragment>();
        private val _fragmentTitleList = ArrayList<String>();

        override fun getItem(position: Int): Fragment {
            return _fragmentList[position];
        }

        override fun getCount(): Int {
            return _fragmentList.size;
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return _fragmentTitleList[position];
        }

        fun addFragment(fragment: Fragment, title: String) {
            _fragmentList.add(fragment);
            _fragmentTitleList.add(title);
        }
    }
}
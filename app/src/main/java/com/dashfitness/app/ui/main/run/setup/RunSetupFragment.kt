package com.dashfitness.app.ui.main.run.setup

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat.getColor
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.ViewModelProvider
import com.dashfitness.app.R
import com.dashfitness.app.RunActivity
import com.dashfitness.app.databinding.FragmentRunSetupBinding
import com.dashfitness.app.ui.main.run.models.RunSegment
import kotlinx.android.synthetic.main.fragment_run_setup.*
import java.util.*


class RunSetupFragment : Fragment() {
    private lateinit var binding: FragmentRunSetupBinding
    private lateinit var viewModel: RunSetupViewModel
    private var whiteColor: Int = R.color.white
    private var darkColor: Int = R.color.colorPrimaryDark
    private lateinit var runSetupCustomFragment: RunSetupCustomFragment
    private lateinit var runSetupTrainingFragment: RunSetupTrainingFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRunSetupBinding.inflate(inflater)
        viewModel = ViewModelProvider(this)[RunSetupViewModel::class.java]
        binding.viewModel = viewModel
        whiteColor = getColor(resources, R.color.white, null)
        darkColor = getColor(resources, R.color.colorPrimaryDark, null)
        setupListeners()

        runSetupCustomFragment = RunSetupCustomFragment(viewModel)
        runSetupTrainingFragment = RunSetupTrainingFragment(viewModel)

        val adapter = ViewPageAdapter(parentFragmentManager)
        adapter.addFragment(runSetupTrainingFragment, "Training")
        adapter.addFragment(runSetupCustomFragment, "Custom")

        binding.runSetupPager.adapter = adapter

        return binding.root
    }

    private fun setupListeners() {
        viewModel.isCustom.observe(requireActivity()) {
            trainingButton.setBackgroundColor(if (it) whiteColor else darkColor)
            trainingButton.setTextColor(if (it) darkColor else whiteColor)
            customButton.setBackgroundColor(if (it) darkColor else whiteColor)
            customButton.setTextColor(if (it) whiteColor else darkColor)
            binding.runSetupPager.currentItem = if (it) 1 else 0
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[RunSetupViewModel::class.java]

        viewModel.navigateToRunActivity.observe(viewLifecycleOwner) { navigate ->
            if (navigate) {
                val intent = Intent(activity, RunActivity::class.java)
                intent.putExtra(
                    "segments",
                    viewModel.segments.value?.let { ArrayList(it) }
                        ?: ArrayList<RunSegment>() as java.io.Serializable)
                startActivity(intent)
                viewModel.onRunNavigated()
            }
        }

        binding.viewModel = viewModel
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

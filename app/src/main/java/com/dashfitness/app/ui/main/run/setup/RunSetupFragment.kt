package com.dashfitness.app.ui.main.run.setup

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.res.ResourcesCompat.getColor
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.dashfitness.app.MainActivity
import com.dashfitness.app.R
import com.dashfitness.app.RunActivity
import com.dashfitness.app.databinding.FragmentRunSetupBinding
import com.dashfitness.app.ui.main.home.HomeFragmentDirections
import com.dashfitness.app.ui.main.run.models.RunSegment
import kotlinx.android.synthetic.main.dialog_add_segment.segmentAmountSlider
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
        viewModel.isTreadmill.observe(requireActivity()) {
            outdoor_run_button.setBackgroundColor(if (it) whiteColor else darkColor)
            outdoor_run_button.setTextColor(if (it) darkColor else whiteColor)
            treadmill_button.setBackgroundColor(if (it) darkColor else whiteColor)
            treadmill_button.setTextColor(if (it) whiteColor else darkColor)
        }
        viewModel.showTreadmillDistanceSegmentsAlert += {
            val builder = requireActivity().let { AlertDialog.Builder(it) }
            builder
                .setTitle("Remove Distance Segments?")
                .setMessage("You currently have 'Distance' type segments in your list, these can only be used for 'Outdoor' (GPS tracked) segments.\n\nWould you like to remove these segments and switch over to 'Treadmill' mode?")
                .setPositiveButton("Remove Segments") { _dialog, _ ->
                    viewModel.removeDistanceSegments()
                    viewModel.isTreadmill.postValue(true)
                    _dialog.dismiss()
                }
                .setNegativeButton("Cancel") { _dialog, _ -> _dialog.dismiss() }
            val dialog = builder.create()
            dialog.show()
        }
    }

    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        run {
            if (result.resultCode == Activity.RESULT_OK) {
                val controller = this.findNavController()
                controller.navigate(RunSetupFragmentDirections.actionDashRunSetupToHome())
                controller.navigate(HomeFragmentDirections.actionHomeToRunDetailFragment(result.data!!.getLongExtra("RunId", 0)))
            }
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
                intent.putExtra(
                    "isTreadmill",
                    viewModel.isTreadmill.value)
                resultLauncher.launch(intent)
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

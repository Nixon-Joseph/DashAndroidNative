package com.dashfitness.app.ui.main.run.setup

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.dashfitness.app.MainActivity
import com.dashfitness.app.R
import com.dashfitness.app.RunActivity
import com.dashfitness.app.databinding.FragmentRunSetupBinding
import com.dashfitness.app.ui.main.home.HomeFragmentDirections
import com.dashfitness.app.ui.main.run.models.RunActivityModel
import com.dashfitness.app.ui.main.run.models.RunSegment
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import kotlin.collections.ArrayList


class RunSetupFragment : Fragment() {
    private lateinit var binding: FragmentRunSetupBinding
    private lateinit var viewModel: RunSetupViewModel
    private var whiteColor: Int = 0
    private var darkColor: Int = 0
    private lateinit var runSetupCustomFragment: RunSetupCustomFragment
    private lateinit var runSetupTrainingFragment: RunSetupTrainingFragment
    private var pendingRun: RunActivityModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRunSetupBinding.inflate(inflater)
        viewModel = ViewModelProvider(this)[RunSetupViewModel::class.java]
        binding.viewModel = viewModel
        whiteColor = resources.getColor(R.color.white, null)
        darkColor = resources.getColor(R.color.colorPrimaryDark, null)
        setupListeners()

        runSetupCustomFragment = RunSetupCustomFragment(viewModel)
        runSetupTrainingFragment = RunSetupTrainingFragment(viewModel, (requireActivity() as MainActivity).runDataDAO)

        val adapter = ViewPageAdapter(parentFragmentManager)
        adapter.addFragment(runSetupTrainingFragment, "Training")
        adapter.addFragment(runSetupCustomFragment, "Custom")

        binding.runSetupPager.adapter = adapter

        return binding.root
    }

    private fun setupListeners() {
        viewModel.isCustom.observe(requireActivity()) {
            binding.trainingButton.setBackgroundColor(if (it) whiteColor else darkColor)
            binding.trainingButton.setTextColor(if (it) darkColor else whiteColor)
            binding.customButton.setBackgroundColor(if (it) darkColor else whiteColor)
            binding.customButton.setTextColor(if (it) whiteColor else darkColor)
            binding.runSetupPager.currentItem = if (it) 1 else 0
        }
        viewModel.isTreadmill.observe(requireActivity()) {
            binding.outdoorRunButton.setBackgroundColor(if (it) whiteColor else darkColor)
            binding.outdoorRunButton.setTextColor(if (it) darkColor else whiteColor)
            binding.treadmillButton.setBackgroundColor(if (it) darkColor else whiteColor)
            binding.treadmillButton.setTextColor(if (it) whiteColor else darkColor)
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
        viewModel.launchRunActivityEvent += {
            var canContinue = true
            if (!viewModel.isTreadmill.value!!) {
                pendingRun = it
                canContinue = requestAndConfirmPermissions()
            }
            if (canContinue) {
                val intent = Intent(activity, RunActivity::class.java)
                intent.putExtra(
                    "segments",
                    it as java.io.Serializable)
                intent.putExtra(
                    "isTreadmill",
                    viewModel.isTreadmill.value)
                resultLauncher.launch(intent)
            }
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { granted ->
        var somethingGranted = false
        granted[Manifest.permission.ACCESS_FINE_LOCATION]?.let { if (it) { somethingGranted = true } }
        granted[Manifest.permission.ACCESS_COARSE_LOCATION]?.let { if (it) { somethingGranted = true } }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            granted[Manifest.permission.ACCESS_BACKGROUND_LOCATION]?.let { if (it) { somethingGranted = true } }
        }
        if (somethingGranted) {
            viewModel.launchRunActivity(pendingRun?.segments ?: ArrayList(), pendingRun?.planRunCode ?: "")
        }
    }

    private fun requestAndConfirmPermissions(): Boolean {
        val perms = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        return if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (
                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) ||
                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                AppSettingsDialog
                    .Builder(this)
                    .setRationale("GPS location permissions have not been granted for this application.\n\nPlease go to settings and enable GPS location permissions to use the tracking features of this app.\n\nIf you only want the segment cues, you can select 'Treadmill' mode, which does not require GPS to function.")
                    .setPositiveButton("Settings")
                    .build()
                    .show()
                false
            } else {
                requestPermissionLauncher.launch(perms)
                false
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q &&
            ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) != PackageManager.PERMISSION_GRANTED)
        {
            val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
            val askPrefString = getString(R.string.ask_background_loc_preference)
            if (prefs.getBoolean(askPrefString, true)) {
                val builder = requireActivity().let { AlertDialog.Builder(it) }
                builder
                    .setTitle("Additional location permission")
                    .setMessage("Location tracking can be inconsistent if only set to \"Allow only when using the app\".\n\nTo make sure your tracking stays consistent on your run, please set the permission to \"Allow all the time\".\n\nThis application will not get your location without your permission, and will never request your location unless you're currently in a tracked run.")
                    .setPositiveButton("OK") { _dialog, _ ->
                        requestPermissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION))
                        _dialog.dismiss()
                    }
                    .setNegativeButton("No") { _dialog, _ ->
                        _dialog.dismiss()
                        val editor = prefs.edit()
                        editor.putBoolean(getString(R.string.ask_background_loc_preference), false)
                        editor.apply()
                    }
                builder.create().show()
                false
            } else {
                true;
            }
        } else {
            true
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

        viewModel.triggerCustomRunActivity.observe(viewLifecycleOwner) { navigate ->
            if (navigate) {
                viewModel.onCustomRunNavigated()
                viewModel.launchRunActivity(viewModel.segments.value?.let { ArrayList(it) } ?: ArrayList<RunSegment>())
            }
        }

        binding.viewModel = viewModel
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
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

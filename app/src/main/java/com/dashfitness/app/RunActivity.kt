package com.dashfitness.app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.ViewModelProvider
import com.dashfitness.app.databinding.ActivityRunBinding
import com.dashfitness.app.database.RunDatabase
import com.dashfitness.app.database.RunDatabaseDao
import com.dashfitness.app.services.TrackingService
import com.dashfitness.app.ui.main.run.models.RunSegment
import com.dashfitness.app.ui.run.RunMapFragment
import com.dashfitness.app.ui.run.RunStatsFragment
import com.dashfitness.app.util.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

@AndroidEntryPoint
class RunActivity : AppCompatActivity() {
    @Inject
    lateinit var dataSource: RunDatabaseDao
    private lateinit var binding: ActivityRunBinding
    private lateinit var viewModel: RunViewModel
    private lateinit var runMapFragment: RunMapFragment
    private lateinit var runStatsFragment: RunStatsFragment
    private lateinit var tts: TextToSpeech
    private var menu: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_run)
        binding.lifecycleOwner = this;
        setSupportActionBar(toolbar);

        val viewModelFactory = DashDBViewModelFactory<Int>(dataSource)
        viewModel = ViewModelProvider(this, viewModelFactory).get(RunViewModel::class.java)
        binding.viewModel = viewModel

        runMapFragment = RunMapFragment(viewModel)
        runStatsFragment = RunStatsFragment(viewModel)

        val adapter = ViewPageAdapter(supportFragmentManager);
        adapter.addFragment(runMapFragment, "Map")
        adapter.addFragment(runStatsFragment, "Stats")

        binding.viewPager.adapter = adapter;
        binding.tabs.setupWithViewPager(binding.viewPager);

        viewModel.endRun += {
            val builder = MaterialAlertDialogBuilder(this)
            builder.setTitle("Are you sure?")
            builder.setMessage("You are about to end your run.\n\nAre you sure you want to proceed?")
            builder.setPositiveButton("End Run") { _, _ ->
//                findViewById<FrameLayout>(R.id.progress_overlay).animateView(View.VISIBLE, 0.4f, 200)
                viewModel.doEndRun()
            }
            builder.setNegativeButton(android.R.string.no) { _, _ -> }
            builder.show()
        }

        tts = TextToSpeech(this, { Log.i("TAG", it.toString()) })

        viewModel.finishActivity += { finish() }

        val segments = intent.getSerializableExtra("segments") as ArrayList<RunSegment>

        viewModel.initialize(
            { r -> /*registerReceiver(r, IntentFilter("LOCATION_CHANGED"))*/},
            { r -> unregisterReceiver(r)},
            { sendCommandToService(Constants.ACTION_START_OR_RESUME_SERVICE) },
            { sendCommandToService(Constants.ACTION_STOP_SERVICE) },
//            { startForegroundServiceCompat(LocationService::class.java, "START_SERVICE") },
//            { startForegroundServiceCompat(LocationService::class.java, "STOP_SERVICE") },
            segments,
            tts,
            savedInstanceState
        )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_tracking_menu, menu)
        this.menu = menu
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        // if currentTimeInMillis > 0L
        // this.menu?.getItem(0)?.isVisible = true // on click - open confirm dialog
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.miCancelTracking -> showCancelTrackingDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showCancelTrackingDialog() {
        val dialog = MaterialAlertDialogBuilder(this)
            .setTitle("Cancel Run")
            .setMessage("Are you sure you want to cancel the current run and delete all its data?")
            .setIcon(R.drawable.ic_baseline_delete_24)
            .setPositiveButton("Yes") { _, _ ->
//                stopRun()
            }.setNegativeButton("No") { dialogInterface, _ ->
                dialogInterface.cancel()
            }
            .create()
        dialog.show()
    }
//
//    private fun stopRun() {
//        sendCommandToService(ACTION_STOP_SERVICE)
//        findNavController().navigate(R.id.action_dash_run_setup_to_home)
//    }

    private fun sendCommandToService(action: String) =
        Intent(this, TrackingService::class.java).also {
            it.action = action
            this.startService(it)
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

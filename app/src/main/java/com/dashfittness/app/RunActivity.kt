package com.dashfittness.app

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dashfittness.app.database.RunDatabase
import com.dashfittness.app.databinding.ActivityRunBinding
import com.dashfittness.app.ui.run.RunMapFragment
import com.dashfittness.app.ui.run.RunStatsFragment
import com.dashfittness.app.util.DashDBViewModelFactory
import com.dashfittness.app.util.LocationService
import com.dashfittness.app.util.animateView
import com.dashfittness.app.util.startForegroundServiceCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_main.*

class RunActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRunBinding;
    private lateinit var viewModel: RunViewModel;
    private lateinit var runMapFragment: RunMapFragment;
    private lateinit var runStatsFragment: RunStatsFragment;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_run)
        binding.lifecycleOwner = this;
        setSupportActionBar(toolbar);
        val dataSource = RunDatabase.getInstance(application).runDatabaseDao

        val viewModelFactory = DashDBViewModelFactory<Int>(dataSource, application)
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

        viewModel.finishActivity += { finish() }

        viewModel.initialize(
            { r -> registerReceiver(r, IntentFilter("LOCATION_CHANGED"))},
            { r -> unregisterReceiver(r)},
            { startForegroundServiceCompat(LocationService::class.java, "START_SERVICE") },
            { startForegroundServiceCompat(LocationService::class.java, "STOP_SERVICE") }
        )
    }

    override fun onDestroy() {
        viewModel.terminateLocationService()
        super.onDestroy()
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

package com.dashfittness.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dashfittness.app.databinding.ActivityRunBinding
import com.dashfittness.app.ui.run.RunMapFragment
import com.dashfittness.app.ui.run.RunStatsFragment
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.android.synthetic.main.main_activity.*

const val PERMISSION_REQUEST: Int = 42;

class RunActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRunBinding;
    private lateinit var fusedLocationClient: FusedLocationProviderClient;
    private lateinit var runMapFragment: RunMapFragment;
    private lateinit var runStatsFragment: RunStatsFragment;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_run)
        setSupportActionBar(toolbar);

        binding.viewModel = ViewModelProvider(this).get(RunViewModel::class.java)

        runMapFragment = RunMapFragment()
        runStatsFragment = RunStatsFragment()

        (binding.viewModel as RunViewModel).stateUpdate.observe(this, Observer {
            runMapFragment.update(it);
            runStatsFragment.update(it)
        })

        val adapter = ViewPageAdapter(supportFragmentManager);
        adapter.addFragment(runMapFragment, "Map")
        adapter.addFragment(runStatsFragment, "Stats")
        binding.viewPager.adapter = adapter;
        binding.tabs.setupWithViewPager(binding.viewPager);

        fusedLocationClient = FusedLocationProviderClient(this);
    }

    override fun onResume() {
        super.onResume()
        binding.viewModel?.startLocationUpdates(this, fusedLocationClient);
    }

    override fun onPause() {
        super.onPause()
        binding.viewModel?.stopLocationUpdates(fusedLocationClient);
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

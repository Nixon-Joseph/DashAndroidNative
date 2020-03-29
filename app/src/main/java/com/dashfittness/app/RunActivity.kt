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
import com.dashfittness.app.util.RunClickInterface
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.android.synthetic.main.main_activity.*

const val PERMISSION_REQUEST: Int = 42;

class RunActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRunBinding;
    private lateinit var viewModel: RunViewModel;
    private lateinit var fusedLocationClient: FusedLocationProviderClient;
    private lateinit var runMapFragment: RunMapFragment;
    private lateinit var runStatsFragment: RunStatsFragment;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_run)
        binding.lifecycleOwner = this;
        setSupportActionBar(toolbar);

        viewModel = ViewModelProvider(this).get(RunViewModel::class.java)
        binding.viewModel = viewModel

        runMapFragment = RunMapFragment(viewModel)
        runStatsFragment = RunStatsFragment(viewModel)

        val adapter = ViewPageAdapter(supportFragmentManager);
        adapter.addFragment(runMapFragment, "Map")
        adapter.addFragment(runStatsFragment, "Stats")
        binding.viewPager.adapter = adapter;
        binding.tabs.setupWithViewPager(binding.viewPager);

        viewModel.locationUpdate.observe(this, Observer { runMapFragment.updateLocation(it) })

        fusedLocationClient = FusedLocationProviderClient(this);
    }

    override fun onResume() {
        super.onResume()
        viewModel?.startLocationUpdates(this, fusedLocationClient);
    }

    override fun onPause() {
        super.onPause()
        viewModel?.stopLocationUpdates(fusedLocationClient);
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

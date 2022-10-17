package com.dashfitness.app

import android.content.Intent
import android.content.IntentFilter
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.ViewModelProvider
import com.dashfitness.app.databinding.ActivityRunBinding
import com.dashfitness.app.database.RunDatabase
import com.dashfitness.app.services.TrackingService
import com.dashfitness.app.ui.main.run.models.RunSegment
import com.dashfitness.app.ui.run.RunMapFragment
import com.dashfitness.app.ui.run.RunStatsFragment
import com.dashfitness.app.util.*
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_main.*

class RunActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRunBinding
    private lateinit var viewModel: RunViewModel
    private lateinit var runMapFragment: RunMapFragment
    private lateinit var runStatsFragment: RunStatsFragment
    private lateinit var tts: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_run)
        binding.lifecycleOwner = this;
        setSupportActionBar(toolbar);
        val dataSource = RunDatabase.getInstance(application).runDatabaseDao

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

        val _receiver = LocationBroadcastReceiver()
        val locs = ArrayList<Location>()
        _receiver.locationReceived += { loc ->
            loc?.let {
                locs.add(loc)
                Log.i("locationReceived_2", "lat: ${loc.latitude}, lon: ${loc.longitude}, accuracy: ${loc.accuracy}, totalCount: ${locs.count()}")
            }
        }
        registerReceiver(_receiver, IntentFilter("LOCATION_CHANGED"))

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

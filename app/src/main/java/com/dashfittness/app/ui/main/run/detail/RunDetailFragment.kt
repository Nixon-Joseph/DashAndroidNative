package com.dashfittness.app.ui.main.run.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.dashfittness.app.R
import com.dashfittness.app.database.RunDatabase
import com.dashfittness.app.database.RunLocationData
import com.dashfittness.app.databinding.FragmentRunDetailBinding
import com.dashfittness.app.util.DashDBViewModelFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import kotlin.collections.ArrayList
import kotlin.math.max
import kotlin.math.min

class RunDetailFragment : Fragment() {
    private lateinit var viewModel: RunDetailViewModel
    private lateinit var binding: FragmentRunDetailBinding
    private var googleMap: GoogleMap? = null
    private var allLocs: ArrayList<LatLng>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRunDetailBinding.inflate(inflater, container, false)

        val application = requireNotNull(this.activity).application
        val arguments = RunDetailFragmentArgs.fromBundle(arguments!!)

        val dataSource = RunDatabase.getInstance(application).runDatabaseDao
        val viewModelFactory = DashDBViewModelFactory(dataSource, application, arguments.runId)
        viewModel = ViewModelProvider(this, viewModelFactory).get(RunDetailViewModel::class.java)

        binding.viewModel = viewModel;

        val mapFragment = childFragmentManager.findFragmentById(R.id.runDetailMapView) as SupportMapFragment;

        mapFragment.getMapAsync {
            googleMap = it
            it.uiSettings.isMyLocationButtonEnabled = false
            it.isMyLocationEnabled = false
            it.uiSettings.isZoomControlsEnabled = false
            it.uiSettings.setAllGesturesEnabled(false)
            setupMapRoute()
        }

        viewModel.runLocs.observe(viewLifecycleOwner, Observer {
            it?.let {
                allLocs = ArrayList()
                it.forEach { locData ->
                    allLocs!!.add(LatLng(locData.latitude, locData.longitude))
                }
                setupMapRoute()
            }
        })

        viewModel.segments.observe(viewLifecycleOwner, Observer {

        })

        return binding.root
    }

    private fun setupMapRoute() {
        if (googleMap != null && allLocs != null) {
            googleMap!!.addPolyline(PolylineOptions().addAll(allLocs))
            var minLat: Double? = null
            var minLng: Double? = null
            var maxLat: Double? = null
            var maxLng: Double? = null
            allLocs!!.forEach {
                if (minLat == null) {
                    minLat = it.latitude
                    maxLat = it.latitude
                    minLng = it.longitude
                    maxLng = it.longitude
                } else {
                    minLat = min(it.latitude, minLat!!)
                    maxLat = max(it.latitude, maxLat!!)
                    minLng = min(it.longitude, minLng!!)
                    maxLng = max(it.longitude, maxLng!!)
                }
            }
            val builder = LatLngBounds.builder()
            builder.include(LatLng(minLat!!, minLng!!))
            builder.include(LatLng(maxLat!!, maxLng!!))
            googleMap!!.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 100))
        }
    }
}

package com.dashfittness.app.ui.run

import android.location.Location
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dashfittness.app.R
import com.dashfittness.app.RunViewModel
import com.dashfittness.app.databinding.RunMapFragmentBinding
import com.dashfittness.app.util.RunClickInterface
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng


class RunMapFragment(runViewModel: RunViewModel) : Fragment() {
    private lateinit var binding: RunMapFragmentBinding
    private var viewModel: RunViewModel = runViewModel
    private lateinit var googleMap: GoogleMap
    private var firstLoc = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = RunMapFragmentBinding.inflate(inflater);
        binding.lifecycleOwner = activity

        val mapFragment = childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment;

        mapFragment.getMapAsync {
            googleMap = it
            googleMap.uiSettings.isMyLocationButtonEnabled = false
            googleMap.isMyLocationEnabled = true
            googleMap.uiSettings.isZoomControlsEnabled = false
            googleMap.uiSettings.setAllGesturesEnabled(true)
            activity?.let {
                var locs = IntArray(2)
                binding.infoLayout.getLocationOnScreen(locs)
                val metrics = DisplayMetrics()
                activity?.windowManager?.defaultDisplay?.getMetrics(metrics)
                googleMap.setPadding(30, 0, 0, metrics.heightPixels - locs[1])
            }
        }

        binding.viewModel = viewModel;

        return binding.root
    }

    fun updateLocation(location: Location?) {
        location?.let {
            if (firstLoc) {
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location!!.latitude, location!!.longitude), 16F))
                firstLoc = false;
            } else {
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location!!.latitude, location!!.longitude), 16F))
            }
        }
    }
}

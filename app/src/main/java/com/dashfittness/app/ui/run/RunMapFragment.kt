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
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng


class RunMapFragment() : Fragment() {
    private lateinit var binding: RunMapFragmentBinding
    private lateinit var viewModel: RunMapViewModel
    private lateinit var googleMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = RunMapFragmentBinding.inflate(inflater);

        val mapFragment = childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment;

        mapFragment.getMapAsync {
            googleMap = it;
            googleMap.uiSettings.isMyLocationButtonEnabled = false;
            googleMap.isMyLocationEnabled = true;
            googleMap.uiSettings.isZoomControlsEnabled = false
            googleMap.uiSettings.setAllGesturesEnabled(true)
            activity?.let {
                var locs = IntArray(2);
                binding.infoLayout.getLocationOnScreen(locs)
                val metrics = DisplayMetrics()
                activity?.windowManager?.defaultDisplay?.getMetrics(metrics)
                googleMap.setPadding(30, 0, 0, metrics.heightPixels - locs[1]);
            }
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RunMapViewModel::class.java)
    }

    fun update(state: RunViewModel.RunState) {
        googleMap?.let {
            state.location?.let {
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(state.location!!.latitude, state.location!!.longitude), 16F))
            }
        }
    }
}

package com.dashfittness.app.ui.run

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dashfittness.app.R
import com.dashfittness.app.RunViewModel
import com.dashfittness.app.databinding.FragmentRunMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions


class RunMapFragment(runViewModel: RunViewModel) : Fragment() {
    private lateinit var binding: FragmentRunMapBinding
    private var viewModel: RunViewModel = runViewModel
    private lateinit var googleMap: GoogleMap
    private var polyLine: Polyline? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRunMapBinding.inflate(inflater);
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
                googleMap.setMaxZoomPreference(16.5f)
                polyLine = googleMap.addPolyline(PolylineOptions())
            }
        }

        viewModel.latLngs.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.let { polyLine?.points = it }
        })

        viewModel.routeBounds.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(it, 100))
        })

        binding.viewModel = viewModel;

        return binding.root
    }
}

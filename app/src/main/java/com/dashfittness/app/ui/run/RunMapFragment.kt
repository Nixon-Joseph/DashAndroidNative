package com.dashfittness.app.ui.run

import android.location.Location
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
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions


class RunMapFragment(runViewModel: RunViewModel) : Fragment() {
    private lateinit var binding: FragmentRunMapBinding
    private var viewModel: RunViewModel = runViewModel
    private lateinit var googleMap: GoogleMap
    private var firstLoc = true

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
            }
        }

        binding.viewModel = viewModel;

        return binding.root
    }

    private val polyLocs = ArrayList<LatLng>()
    private lateinit var polyLine: Polyline
    fun updateLocation(location: Location?) {
        location?.let {
            polyLocs.add(LatLng(location!!.latitude, location!!.longitude))
            if (firstLoc) {
                polyLine = googleMap.addPolyline(PolylineOptions().addAll(polyLocs))
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(polyLocs.last(), 16F))
                firstLoc = false;
            } else {
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(polyLocs.last(), 16F))
                polyLine.points = polyLocs
            }
        }
    }
}

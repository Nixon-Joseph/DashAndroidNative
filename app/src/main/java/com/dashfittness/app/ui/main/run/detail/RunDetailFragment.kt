package com.dashfittness.app.ui.main.run.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.dashfittness.app.R
import com.dashfittness.app.databinding.FragmentRunDetailBinding
import com.google.android.gms.maps.SupportMapFragment

class RunDetailFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentRunDetailBinding.inflate(inflater, container, false)

        val mapFragment = childFragmentManager.findFragmentById(R.id.runDetailMapView) as SupportMapFragment;

        mapFragment.getMapAsync {
            val googleMap = it
            googleMap.uiSettings.isMyLocationButtonEnabled = false
            googleMap.isMyLocationEnabled = false
            googleMap.uiSettings.isZoomControlsEnabled = false
            googleMap.uiSettings.setAllGesturesEnabled(false)
        }

        return binding.root
    }
}

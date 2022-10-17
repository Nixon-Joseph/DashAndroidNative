package com.dashfitness.app.ui.run

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Build.VERSION_CODES.S
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getMainExecutor
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.dashfitness.app.R
import com.dashfitness.app.RunViewModel
import com.dashfitness.app.databinding.FragmentRunMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.dashfitness.app.services.Polyline
import com.dashfitness.app.services.TrackingService
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.model.LatLngBounds
import kotlinx.android.synthetic.main.fragment_run_map.*
import timber.log.Timber


class RunMapFragment(runViewModel: RunViewModel) : Fragment() {
    private lateinit var binding: FragmentRunMapBinding
    private var viewModel: RunViewModel = runViewModel
    private lateinit var googleMap: GoogleMap

    private var isTracking = false
    private var pathPoints = mutableListOf<Polyline>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRunMapBinding.inflate(inflater)
        binding.lifecycleOwner = activity

        val mapFragment = childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment

        mapFragment.getMapAsync {
            googleMap = it
            googleMap.uiSettings.isMyLocationButtonEnabled = false
            googleMap.isMyLocationEnabled = ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            googleMap.uiSettings.isZoomControlsEnabled = false
            googleMap.uiSettings.setAllGesturesEnabled(true)
            activity?.let {
                val locs = IntArray(2)
                binding.infoLayout.getLocationOnScreen(locs)
                val metrics = DisplayMetrics()
                activity?.windowManager?.defaultDisplay?.getMetrics(metrics)
                googleMap.setPadding(30, 0, 0, metrics.heightPixels - locs[1])
                googleMap.setMaxZoomPreference(16.5f)
                addAllPolylines()
            }
        }
        viewModel.routeBounds.observe(viewLifecycleOwner) {
            googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(it, 100))
        }

        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToObservers()
    }

    private fun subscribeToObservers() {
        TrackingService.isTracking.observe(viewLifecycleOwner, Observer {
            updateTracking(it)
        })

        TrackingService.pathPoints.observe(viewLifecycleOwner, Observer {
            pathPoints = it
            addLatestPolyline()
            moveCameraToUser()
            Log.d("ASD", "HIT")
        })

        TrackingService.timeRunInMillis.observe(viewLifecycleOwner, Observer {

        })
    }

    private fun updateTracking(isTracking: Boolean) {
        this.isTracking = isTracking
    }

    private fun moveCameraToUser() {
        if (pathPoints.isNotEmpty() && pathPoints.last().isNotEmpty()) {
//            val builder = LatLngBounds.builder()
//            builder.include(LatLng(viewModel.minLat!!, viewModel.minLng!!))
//            builder.include(LatLng(viewModel.maxLat!!, viewModel.maxLng!!))
//            googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 100))
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pathPoints.last().last(), 15f))
        }
    }

    private fun addAllPolylines() {
        for (polyline in pathPoints) {
            val polylineOptions = PolylineOptions()
                .color(R.color.colorAccent)
                .width(8f)
                .addAll(polyline)
            googleMap.addPolyline(polylineOptions)
        }
    }

    private fun addLatestPolyline() {
        if (pathPoints.isNotEmpty() && pathPoints.last().size > 1) {
            val preLastLatLng = pathPoints.last()[pathPoints.last().size - 2]
            val lastLatLng = pathPoints.last().last()
            val polylineOptions = PolylineOptions()
                .color(R.color.colorAccent)
                .width(8f)
                .add(preLastLatLng)
                .add(lastLatLng)
            googleMap.addPolyline(polylineOptions)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    @SuppressLint("MissingPermission")
    override fun onStart() {
        super.onStart()
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment

        val locationManager = context?.getSystemService<LocationManager>()
        locationManager?.getCurrentLocation(
            if (Build.VERSION.SDK_INT >= S) { LocationManager.FUSED_PROVIDER } else { LocationManager.GPS_PROVIDER },
            null,
            getMainExecutor(requireContext())
        ) { location ->
            mapFragment.getMapAsync { map ->
                map.moveCamera(
                    CameraUpdateFactory.newCameraPosition(
                        CameraPosition(
                            LatLng(
                                location.latitude,
                                location.longitude
                            ), 16.5f, 0f, 0f
                        )
                    )
                )
            }
        }
    }
}

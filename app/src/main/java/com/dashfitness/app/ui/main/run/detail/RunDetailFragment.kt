package com.dashfitness.app.ui.main.run.detail

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.dashfitness.app.R
import com.dashfitness.app.database.RunDatabase
import com.dashfitness.app.databinding.FragmentRunDetailBinding
import com.dashfitness.app.util.DashDBViewModelFactory
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
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
    private lateinit var allElevations: ArrayList<Float>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentRunDetailBinding.inflate(inflater, container, false)

        val application = requireNotNull(this.activity).application
        val arguments = RunDetailFragmentArgs.fromBundle(requireArguments())

        val dataSource = RunDatabase.getInstance(application).runDatabaseDao
        val viewModelFactory = DashDBViewModelFactory(dataSource, arguments.runId)
        viewModel = ViewModelProvider(this, viewModelFactory)[RunDetailViewModel::class.java]

        binding.viewModel = viewModel

        val mapFragment = childFragmentManager.findFragmentById(R.id.runDetailMapView) as SupportMapFragment

        mapFragment.getMapAsync {
            googleMap = it
            it.uiSettings.isMyLocationButtonEnabled = false
            it.uiSettings.isZoomControlsEnabled = false
            it.uiSettings.setAllGesturesEnabled(false)
            setupMapRoute()
        }

        viewModel.runLocs.observe(viewLifecycleOwner) {
            it?.let {
                allElevations = ArrayList()
                allLocs = ArrayList()
                it.forEach { locData ->
                    allLocs!!.add(LatLng(locData.latitude, locData.longitude))
                    allElevations.add(locData.altitude.toFloat())
                }
                allElevations = smoothElevations(allElevations)
                setupMapRoute()
                val entryList = ArrayList<Entry>()
                allElevations.forEachIndexed { i, d -> entryList.add(Entry(i.toFloat(), d)); }
                val dataSet = LineDataSet(entryList, "")
                dataSet.setDrawCircles(false)
                dataSet.setDrawValues(false)
                dataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
                val lineData = LineData(dataSet)
                binding.elevationChart.data = lineData
                binding.elevationChart.isDoubleTapToZoomEnabled = false
                binding.elevationChart.isScaleYEnabled = false
                binding.elevationChart.isScaleXEnabled = false
                binding.elevationChart.legend.isEnabled = false
                binding.elevationChart.description.isEnabled = false
                binding.elevationChart.setPinchZoom(false)
                binding.elevationChart.invalidate()
            }
        }

        viewModel.segments.observe(viewLifecycleOwner) {
//            val hrVals = listOf(80f, 80f, 90f, 100f, 120f, 120f, 120f, 120f, 110f, 110f, 90f, 80f)
//            val entryList = ArrayList<Entry>()
//            hrVals.forEachIndexed { i, d ->
//                entryList.add(Entry(i.toFloat(), d));
//            }
//            val dataSet = LineDataSet(entryList, "")
//            val lineData = LineData(dataSet)
//            binding.elevationChart.data = lineData
//            binding.elevationChart.isDoubleTapToZoomEnabled = false
//            binding.elevationChart.isScaleYEnabled = false
//            binding.elevationChart.isScaleXEnabled = false
//            binding.elevationChart.legend.isEnabled = false
//            binding.elevationChart.description.isEnabled = false
//            binding.elevationChart.setPinchZoom(false)
//            binding.elevationChart.invalidate()
//            binding.heartRateChart.data = lineData
//            binding.heartRateChart.isDoubleTapToZoomEnabled = false
//            binding.heartRateChart.isScaleYEnabled = false
//            binding.heartRateChart.isScaleXEnabled = false
//            binding.heartRateChart.legend.isEnabled = false
//            binding.heartRateChart.description.isEnabled = false
//            binding.heartRateChart.setPinchZoom(false)
//            binding.heartRateChart.invalidate()
//            binding.paceChart.data = lineData
//            binding.paceChart.isDoubleTapToZoomEnabled = false
//            binding.paceChart.isScaleYEnabled = false
//            binding.paceChart.isScaleXEnabled = false
//            binding.paceChart.legend.isEnabled = false
//            binding.paceChart.description.isEnabled = false
//            binding.paceChart.setPinchZoom(false)
//            binding.paceChart.invalidate()
        }

        binding.collapsingAppBar.setExpanded(false, false)

        return binding.root
    }

    private fun smoothElevations(elevationList: ArrayList<Float>): ArrayList<Float> {
        return if (elevationList.size >= 100) {
            val newList = ArrayList<Float>()
            for (i in 0 until elevationList.size step 20) {
                var curTotal = 0f
                var count = 0f
                for (i2 in max(0, i - 10)..min(elevationList.size - 1, i + 10)) {
                    curTotal += elevationList[i2]
                    count++
                }
                newList.add(curTotal / count)
            }
            newList
        } else {
            elevationList
        }
    }

    private fun setupMapRoute() {
        if (googleMap != null && allLocs != null) {
            googleMap!!.addPolyline(PolylineOptions().addAll(allLocs!!))
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
            binding.collapsingAppBar.setExpanded(true, true)
        }
    }
}

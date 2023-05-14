package com.dashfitness.app.ui.main.run.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.dashfitness.app.R
import com.dashfitness.app.database.RunDatabaseDao
import com.dashfitness.app.databinding.FragmentRunDetailBinding
import com.dashfitness.app.util.RunDetailViewModelFactory
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.math.max
import kotlin.math.min

@AndroidEntryPoint
class RunDetailFragment : Fragment() {
    @Inject
    lateinit var database: RunDatabaseDao
    private val args: RunDetailFragmentArgs by navArgs()
    val viewModelFactory: RunDetailViewModelFactory by lazy {
        RunDetailViewModelFactory(database, args.runId)
    }
    private val viewModel: RunDetailViewModel by viewModels { viewModelFactory }
    private lateinit var binding: FragmentRunDetailBinding
    private var googleMap: GoogleMap? = null
    private var pathPoints: ArrayList<ArrayList<LatLng>>? = null
    private lateinit var allElevations: ArrayList<Float>
    @Inject
    lateinit var dataSource: RunDatabaseDao

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentRunDetailBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = activity
        binding.viewModel = viewModel

        val mapFragment = childFragmentManager.findFragmentById(R.id.runDetailMapView) as SupportMapFragment

        mapFragment.getMapAsync {
            googleMap = it
            it.uiSettings.isMyLocationButtonEnabled = false
            it.uiSettings.isZoomControlsEnabled = false
            it.uiSettings.setAllGesturesEnabled(false)
            setupMapRoute()
        }

        viewModel.run.observe(viewLifecycleOwner) {
            viewModel.totalDistance.postValue(it.totalDistance)
        }

        viewModel.runLocs.observe(viewLifecycleOwner) {
            it?.let {
                allElevations = ArrayList()
                pathPoints = ArrayList()
                it.groupBy {
                        x -> x.polylineIndex
                }.forEach { locDataList ->
                    val collection = arrayListOf<LatLng>()
                    for (runLocationData in locDataList.value) {
                        collection.add(LatLng(runLocationData.latitude, runLocationData.longitude))
                        allElevations.add(runLocationData.altitude.toFloat())
                    }
                    pathPoints!!.add(collection)
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
        if (googleMap != null && pathPoints != null) {
            var minLat: Double? = null
            var minLng: Double? = null
            var maxLat: Double? = null
            var maxLng: Double? = null
            pathPoints!!.forEach { latLngList ->
                googleMap!!.addPolyline(PolylineOptions().addAll(latLngList))

                latLngList.forEach { latLng ->
                    if (minLat == null) {
                        minLat = latLng.latitude
                        maxLat = latLng.latitude
                        minLng = latLng.longitude
                        maxLng = latLng.longitude
                    } else {
                        minLat = min(latLng.latitude, minLat!!)
                        maxLat = max(latLng.latitude, maxLat!!)
                        minLng = min(latLng.longitude, minLng!!)
                        maxLng = max(latLng.longitude, maxLng!!)
                    }
                }
            }
            val builder = LatLngBounds.builder()
            minLng?.let {
                builder.include(LatLng(minLat!!, minLng!!))
                builder.include(LatLng(maxLat!!, maxLng!!))
                googleMap!!.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 100))
            }
            binding.collapsingAppBar.setExpanded(true, true)
        }
    }
}

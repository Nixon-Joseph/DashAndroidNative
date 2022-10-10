package com.dashfitness.app.ui.main.run.setup

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.FrameLayout
import androidx.core.content.res.ResourcesCompat.getColor
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dashfitness.app.R
import com.dashfitness.app.RunActivity
import com.dashfitness.app.databinding.FragmentRunSetupBinding
import com.dashfitness.app.ui.main.run.models.RunSegment
import com.dashfitness.app.ui.main.run.models.RunSegmentSpeed
import com.dashfitness.app.ui.main.run.models.RunSegmentType
import com.dashfitness.app.util.animateView
import com.google.android.material.slider.Slider
import com.kevincodes.recyclerview.ItemDecorator
import kotlinx.android.synthetic.main.fragment_run_setup.*
import java.util.*
import kotlin.collections.ArrayList


class RunSetupFragment : Fragment() {
    private lateinit var binding: FragmentRunSetupBinding
    private lateinit var viewModel: RunSetupViewModel
    private var newSegmentType: RunSegmentType = RunSegmentType.TIME
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var itemTouchHelper: ItemTouchHelper

    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_open_anim) }
    private val rotateClosed: Animation by lazy { AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_close_anim) }
    private val fromBottom: Animation by lazy { AnimationUtils.loadAnimation(requireContext(), R.anim.from_bottom_anim) }
    private val toBottom: Animation by lazy { AnimationUtils.loadAnimation(requireContext(), R.anim.to_bottom_anim) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRunSetupBinding.inflate(inflater);
        viewModel = ViewModelProvider(this)[RunSetupViewModel::class.java]
        binding.viewModel = viewModel
        viewModel.addSegmentClicked += { clicked -> onAddSegmentButtonClicked(clicked) }
        viewModel.addRunSegmentClicked += { segmentSpeed -> showSegmentDialog(
            segmentSpeed,
            inflater,
            null,
            null,
            null
        ) }
        setupSegmentList(inflater)

        return binding.root;
    }

    private fun setupSegmentList(inflater: LayoutInflater) {
        val adapter = SegmentSetupAdapter(SegmentSetupListener { segment ->
            showSegmentDialog(segment.speed, inflater, segment.type, segment.value, segment.id)
        })
        val simpleCallback = object :
            ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {

                ItemDecorator.Builder(c, recyclerView, viewHolder, dX, actionState).set(
                    backgroundColorFromStartToEnd = getColor(resources, R.color.red, null),
                    backgroundColorFromEndToStart = getColor(resources, R.color.red, null),
                    textFromStartToEnd = "Remove",
                    textFromEndToStart = "Remove",
                    textColorFromStartToEnd = getColor(resources, R.color.white, null),
                    textColorFromEndToStart = getColor(resources, R.color.white, null),
                    iconTintColorFromStartToEnd = 0,
                    iconTintColorFromEndToStart = 0,
                    textSizeFromStartToEnd = 16f,
                    textSizeFromEndToStart = 16f,
                    typeFaceFromStartToEnd = Typeface.DEFAULT_BOLD,
                    typeFaceFromEndToStart = Typeface.SANS_SERIF,
                    iconResIdFromStartToEnd = R.drawable.ic_baseline_delete_24,
                    iconResIdFromEndToStart = R.drawable.ic_baseline_delete_24
                )

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.layoutPosition
                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        viewModel.removeSegmentAt(position)
                    }
                    ItemTouchHelper.RIGHT -> {
                        viewModel.removeSegmentAt(position)
                    }
                }
            }
        }
        itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(binding.segmentList)
        linearLayoutManager = LinearLayoutManager(requireActivity())
        binding.segmentList.layoutManager = linearLayoutManager

        viewModel.segments.observe(viewLifecycleOwner) {
            it?.let {
                adapter.submitList(it)
            }
        }
        binding.lifecycleOwner = this
        binding.segmentList.adapter = adapter
    }

    private fun onAddSegmentButtonClicked(clicked: Boolean) {
        setVisibility(clicked)
        setAnimation(clicked)
        setClickable(clicked)
    }

    private fun setVisibility(clicked: Boolean) {
        if (!clicked) {
            runButton.visibility = View.VISIBLE
            walkButton.visibility = View.VISIBLE
        } else {
            runButton.visibility = View.INVISIBLE
            walkButton.visibility = View.INVISIBLE
        }
    }

    private fun setAnimation(clicked: Boolean) {
        if (!clicked) {
            runButton.startAnimation(fromBottom)
            walkButton.startAnimation(fromBottom)
            addSegmentButton.startAnimation(rotateOpen)
        } else {
            runButton.startAnimation(toBottom)
            walkButton.startAnimation(toBottom)
            addSegmentButton.startAnimation(rotateClosed)
        }
    }

    private fun setClickable(clicked: Boolean) {
        runButton.isClickable = !clicked
        walkButton.isClickable = !clicked
    }

    override fun onResume() {
        super.onResume()
        activity?.findViewById<FrameLayout>(R.id.progress_overlay)?.animateView(View.GONE, 0.0f, 0)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RunSetupViewModel::class.java)

        viewModel.navigateToRunActivity.observe(viewLifecycleOwner, Observer { navigate ->
            if (navigate) {
                val intent = Intent(activity, RunActivity::class.java)
                intent.putExtra("segments", viewModel.segments.value?.let { ArrayList(it) } ?: ArrayList<RunSegment>() as java.io.Serializable)
                startActivity(intent);
                viewModel.onRunNavigated();
            }
        })

        binding.viewModel = viewModel;
    }

    private fun showSegmentDialog(
        segmentSpeed: RunSegmentSpeed,
        inflater: LayoutInflater,
        segmentType: RunSegmentType?,
        segmentValue: Float?,
        id: UUID?
    ) {
        val builder = requireActivity().let { AlertDialog.Builder(it) }
        var title = "Add "
        if (segmentSpeed == RunSegmentSpeed.Run) {
            title += "Run"
        } else {
            title += "Walk"
        }
        val view = inflater.inflate(R.layout.dialog_add_segment, null)
        builder
            .setTitle(title)
            .setView(view)
            .setPositiveButton("Confirm") { _dialog, _ ->
                viewModel.addSegment(newSegmentType, segmentSpeed, view.findViewById<Slider>(
                    R.id.segmentAmountSlider).value)
                _dialog.dismiss()
            }
            .setNegativeButton("Cancel") { _dialog, _ -> _dialog.dismiss() }
        val distanceButton = view.findViewById<Button>(R.id.segmentDistanceButton)
        val timeButton = view.findViewById<Button>(R.id.segmentTimeButton)
        newSegmentType = RunSegmentType.TIME
        segmentType?.let {
            newSegmentType = segmentType
            handleSegmentTypeChange(it, timeButton, distanceButton, view)
        }
        segmentValue?.let {
            view.findViewById<Slider>(R.id.segmentAmountSlider).value = it
        }
        id?.let {
            builder.setPositiveButton("Confirm") { _dialog, _ ->
                viewModel.editSegment(it, newSegmentType, segmentSpeed, view.findViewById<Slider>(R.id.segmentAmountSlider).value)
                _dialog.dismiss()
            }
        }
        val dialog = builder.create()
        timeButton.setOnClickListener {
            handleSegmentTypeChange(RunSegmentType.TIME, timeButton, distanceButton, view)
            newSegmentType = RunSegmentType.TIME
        }
        distanceButton.setOnClickListener {
            handleSegmentTypeChange(RunSegmentType.DISTANCE, timeButton, distanceButton, view)
            newSegmentType = RunSegmentType.DISTANCE
        }
        dialog.show()
    }

    private fun handleSegmentTypeChange(segmentType: RunSegmentType, timeButton: Button, distanceButton: Button, view: View) {
        val slider = view.findViewById<Slider>(R.id.segmentAmountSlider)
        when (segmentType) {
            RunSegmentType.TIME -> {
                slider.value = 5f
                slider.valueFrom = 1f
                slider.valueTo = 60f
                slider.stepSize = 1f
                timeButton.setBackgroundColor(getColor(resources, R.color.colorPrimaryDark, null))
                timeButton.setTextColor(getColor(resources, R.color.white, null))
                distanceButton.setBackgroundColor(getColor(resources, R.color.white, null))
                distanceButton.setTextColor(getColor(resources, R.color.colorPrimaryDark, null))
            }
            else -> {
                slider.value = 1f
                slider.valueFrom = 0.25f
                slider.valueTo = 5f
                slider.stepSize = 0.25f
                distanceButton.setBackgroundColor(getColor(resources, R.color.colorPrimaryDark, null))
                distanceButton.setTextColor(getColor(resources, R.color.white, null))
                timeButton.setBackgroundColor(getColor(resources, R.color.white, null))
                timeButton.setTextColor(getColor(resources, R.color.colorPrimaryDark, null))
            }
        }
    }
}

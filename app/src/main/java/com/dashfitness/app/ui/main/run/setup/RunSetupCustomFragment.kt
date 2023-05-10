package com.dashfitness.app.ui.main.run.setup

import android.app.AlertDialog
import android.graphics.Canvas
import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dashfitness.app.R
import com.dashfitness.app.databinding.FragmentRunSetupCustomBinding
import com.dashfitness.app.ui.main.run.models.RunSegmentSpeed
import com.dashfitness.app.ui.main.run.models.RunSegmentType
import com.google.android.material.button.MaterialButton
import com.google.android.material.slider.Slider
import com.google.android.material.textfield.TextInputEditText
import com.kevincodes.recyclerview.ItemDecorator
import java.util.*

class RunSetupCustomFragment(private val viewModel: RunSetupViewModel) : Fragment() {
    private lateinit var binding: FragmentRunSetupCustomBinding
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var itemTouchHelper: ItemTouchHelper
    private var newSegmentType: RunSegmentType = RunSegmentType.TIME
    private var whiteColor: Int = 0
    private var darkColor: Int = 0
    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_open_anim) }
    private val rotateClosed: Animation by lazy { AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_close_anim) }
    private val fromBottom: Animation by lazy { AnimationUtils.loadAnimation(requireContext(), R.anim.from_bottom_anim) }
    private val toBottom: Animation by lazy { AnimationUtils.loadAnimation(requireContext(), R.anim.to_bottom_anim) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRunSetupCustomBinding.inflate(inflater)
        binding.lifecycleOwner = activity
        binding.viewModel = viewModel
        linearLayoutManager = LinearLayoutManager(requireActivity())
        whiteColor = resources.getColor(R.color.white, null)
        darkColor = resources.getColor(R.color.colorPrimaryDark, null)
        binding.segmentList.layoutManager = linearLayoutManager
        setupSegmentList(inflater)
        setupListeners(inflater)

        return binding.root
    }

    private fun setVisibility(clicked: Boolean) {
        if (!clicked) {
            binding.runButton.visibility = View.VISIBLE
            binding.walkButton.visibility = View.VISIBLE
            binding.alertButton.visibility = View.VISIBLE
        } else {
            binding.runButton.visibility = View.INVISIBLE
            binding.walkButton.visibility = View.INVISIBLE
            binding.alertButton.visibility = View.INVISIBLE
        }
    }

    private fun setAnimation(clicked: Boolean) {
        if (!clicked) {
            binding.runButton.startAnimation(fromBottom)
            binding.walkButton.startAnimation(fromBottom)
            binding.alertButton.startAnimation(fromBottom)
            binding.addSegmentButton.startAnimation(rotateOpen)
        } else {
            binding.runButton.startAnimation(toBottom)
            binding.walkButton.startAnimation(toBottom)
            binding.alertButton.startAnimation(toBottom)
            binding.addSegmentButton.startAnimation(rotateClosed)
        }
    }

    private fun setClickable(clicked: Boolean) {
        binding.runButton.isClickable = !clicked
        binding.walkButton.isClickable = !clicked
        binding.alertButton.isClickable = !clicked
    }

    private fun setupListeners(inflater: LayoutInflater) {
        viewModel.addSegmentClicked += { clicked -> onAddSegmentButtonClicked(clicked) }
        viewModel.addRunSegmentClicked += { segmentSpeed -> showSegmentDialog(
            segmentSpeed,
            inflater,
            null,
            null,
            null
        ) }
        viewModel.addRunAlertSegmentClicked += { showAlertSegmentDialog(
            inflater,
            null,
            null
        ) }
    }

    private fun setupSegmentList(inflater: LayoutInflater) {
        val adapter = SegmentSetupAdapter(SegmentSetupListener { segment ->
            if (segment.type == RunSegmentType.ALERT) {
                showAlertSegmentDialog(inflater, segment.text, segment.id)
            } else {
                showSegmentDialog(segment.speed, inflater, segment.type, segment.value, segment.id)
            }
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
                    backgroundColorFromStartToEnd = ResourcesCompat.getColor(
                        resources,
                        R.color.red,
                        null
                    ),
                    backgroundColorFromEndToStart = ResourcesCompat.getColor(
                        resources,
                        R.color.red,
                        null
                    ),
                    textFromStartToEnd = "Remove",
                    textFromEndToStart = "Remove",
                    textColorFromStartToEnd = whiteColor,
                    textColorFromEndToStart = whiteColor,
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

    private fun showSegmentDialog(
        segmentSpeed: RunSegmentSpeed,
        inflater: LayoutInflater,
        segmentType: RunSegmentType?,
        segmentValue: Float?,
        id: UUID?
    ) {
        val builder = requireActivity().let { AlertDialog.Builder(it) }
        var title = "Add "
        title += if (segmentSpeed == RunSegmentSpeed.RUN) {
            "Run"
        } else {
            "Walk"
        }
        val view = inflater.inflate(R.layout.dialog_add_segment, null)
        builder
            .setTitle(title)
            .setView(view)
            .setPositiveButton("Confirm") { _dialog, _ ->
                viewModel.addSegment(newSegmentType, segmentSpeed, view.findViewById<Slider>(R.id.segmentAmountSlider).value)
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
        viewModel.isTreadmill.value?.let {
            if (it) {
                distanceButton.isEnabled = false
            } else {
                distanceButton.setOnClickListener {
                    handleSegmentTypeChange(RunSegmentType.DISTANCE, timeButton, distanceButton, view)
                    newSegmentType = RunSegmentType.DISTANCE
                }
            }
        }
        dialog.show()
    }

    private fun showAlertSegmentDialog(inflater: LayoutInflater, segmentText: String?, id: UUID?) {
        val builder = requireActivity().let { AlertDialog.Builder(it) }
        val view = inflater.inflate(R.layout.dialog_add_alert_segment, null)
        builder
            .setTitle("Add Alert")
            .setView(view)
            .setPositiveButton("Confirm Custom") { _dialog, _ ->
                val text = view.findViewById<TextInputEditText>(R.id.customAlertText).text.toString()
                if (text.isNotEmpty()) {
                    viewModel.addSegment(RunSegmentType.ALERT, RunSegmentSpeed.NONE, 0f, text, true)
                    _dialog.dismiss()
                }
            }
            .setNegativeButton("Cancel") { _dialog, _ -> _dialog.dismiss() }
        id?.let {
            builder.setPositiveButton("Confirm Custom") { _dialog, _ ->
                val text = view.findViewById<TextInputEditText>(R.id.customAlertText).text.toString()
                if (text.isNotEmpty()) {
                    viewModel.editSegment(it, RunSegmentType.ALERT, RunSegmentSpeed.NONE, 0f, text, true)
                    _dialog.dismiss()
                }
            }
        }
        segmentText?.let {
            view.findViewById<TextInputEditText>(R.id.customAlertText).setText(it);
        }
        val dialog = builder.create()
        view.findViewById<MaterialButton>(R.id.halfwayButton).setOnClickListener {
            viewModel.addSegment(RunSegmentType.ALERT, RunSegmentSpeed.NONE, 0f, "Halfway There", false)
            dialog.dismiss()
        }
        view.findViewById<MaterialButton>(R.id.goodJobButton).setOnClickListener {
            viewModel.addSegment(RunSegmentType.ALERT, RunSegmentSpeed.NONE, 0f, "Good Job", false)
            dialog.dismiss()
        }
        view.findViewById<MaterialButton>(R.id.keepGoingButton).setOnClickListener {
            viewModel.addSegment(RunSegmentType.ALERT, RunSegmentSpeed.NONE, 0f, "Keep Going", false)
            dialog.dismiss()
        }
        view.findViewById<MaterialButton>(R.id.almostThereButton).setOnClickListener {
            viewModel.addSegment(RunSegmentType.ALERT, RunSegmentSpeed.NONE, 0f, "Almost there", false)
            dialog.dismiss()
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
                timeButton.setBackgroundColor(darkColor)
                timeButton.setTextColor(whiteColor)
                viewModel.isTreadmill.value?.let {
                    if (!it) {
                        distanceButton.setBackgroundColor(whiteColor)
                        distanceButton.setTextColor(darkColor)
                    }
                }
            }
            else -> {
                slider.value = 1f
                slider.valueFrom = 0.25f
                slider.valueTo = 5f
                slider.stepSize = 0.25f
                viewModel.isTreadmill.value?.let {
                    if (!it) {
                        distanceButton.setBackgroundColor(darkColor)
                        distanceButton.setTextColor(whiteColor)
                    }
                }
                timeButton.setBackgroundColor(whiteColor)
                timeButton.setTextColor(darkColor)
            }
        }
    }

}
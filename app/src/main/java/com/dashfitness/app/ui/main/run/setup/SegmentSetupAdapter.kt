package com.dashfitness.app.ui.main.run.setup

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dashfitness.app.databinding.ListItemRunSetupSegmentBinding
import com.dashfitness.app.ui.main.run.models.RunSegment

class SegmentSetupAdapter(private val segmentSetupListener: SegmentSetupListener) : ListAdapter<RunSegment, RecyclerView.ViewHolder>(SegmentSetupDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                holder.bind(getItem(position)!!, segmentSetupListener)
            }
        }
    }

    class ViewHolder private constructor(val binding: ListItemRunSetupSegmentBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: RunSegment, clickListener: SegmentSetupListener) {
            binding.segment = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemRunSetupSegmentBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class SegmentSetupDiffCallback : DiffUtil.ItemCallback<RunSegment>() {
    override fun areContentsTheSame(oldItem: RunSegment, newItem: RunSegment): Boolean {
        return oldItem.speed == newItem.speed && oldItem.type == newItem.type
    }

    override fun areItemsTheSame(oldItem: RunSegment, newItem: RunSegment): Boolean {
        return oldItem.id == newItem.id
    }
}

class SegmentSetupListener(val clickListener: (segment: RunSegment) -> Unit) {
    fun onClick(segment: RunSegment) = clickListener(segment)
}
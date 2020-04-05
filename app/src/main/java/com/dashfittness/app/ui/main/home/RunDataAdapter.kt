package com.dashfittness.app.ui.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dashfittness.app.database.RunData
import com.dashfittness.app.databinding.ListItemRunDataBinding
import com.dashfittness.app.util.convertLongToTimeString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RunDataAdapter(private val runDataListener: RunDataListener) : ListAdapter<RunData, RecyclerView.ViewHolder>(RunDataDiffCallback()) {
    private val adapterScope = CoroutineScope(Dispatchers.Default);

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                holder.bind(getItem(position)!!, runDataListener)
            }
        }
    }

    class ViewHolder private constructor(val binding: ListItemRunDataBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: RunData, clickListener: RunDataListener) {
            binding.run = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemRunDataBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class RunDataDiffCallback : DiffUtil.ItemCallback<RunData>() {
    override fun areContentsTheSame(oldItem: RunData, newItem: RunData): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areItemsTheSame(oldItem: RunData, newItem: RunData): Boolean {
        return oldItem == newItem
    }
}

class RunDataListener(val clickListener: (run: RunData) -> Unit) {
    fun onClick(runData: RunData) = clickListener(runData)
}
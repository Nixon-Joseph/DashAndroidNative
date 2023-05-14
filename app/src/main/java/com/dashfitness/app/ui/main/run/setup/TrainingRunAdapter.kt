package com.dashfitness.app.ui.main.run.setup

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dashfitness.app.databinding.ListItemTrainingRunBinding
import com.dashfitness.app.training.ITrainingRun
import com.dashfitness.app.training.TrainingRun
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class TrainingRunAdapter(private val trainingRunListener: TrainingRunListener) : ListAdapter<ITrainingRun, RecyclerView.ViewHolder>(
    TrainingRunDiffCallback()
) {
    private val adapterScope = CoroutineScope(Dispatchers.Default);

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                holder.bind(getItem(position)!!, trainingRunListener)
            }
        }
    }

    class ViewHolder private constructor(val binding: ListItemTrainingRunBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: ITrainingRun, clickListener: TrainingRunListener) {
            binding.run = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemTrainingRunBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class TrainingRunDiffCallback : DiffUtil.ItemCallback<ITrainingRun>() {
    override fun areContentsTheSame(oldItem: ITrainingRun, newItem: ITrainingRun): Boolean {
        return oldItem.Code == newItem.Code && oldItem.FinishedRun == newItem.FinishedRun
    }

    override fun areItemsTheSame(oldItem: ITrainingRun, newItem: ITrainingRun): Boolean {
        return oldItem == newItem
    }
}

class TrainingRunListener(val clickListener: (run: ITrainingRun) -> Unit) {
    fun onClick(run: ITrainingRun) = clickListener(run)
}
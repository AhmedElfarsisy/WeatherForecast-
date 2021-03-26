package com.iti.elfarsisy.mad41.myapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.iti.elfarsisy.mad41.myapplication.data.model.DailyItem
import com.iti.elfarsisy.mad41.myapplication.databinding.DailyItemBinding
import timber.log.Timber

class DailyAdapter : ListAdapter<DailyItem, DailyAdapter.DailyHolder>(DiffCallBack) {
    class DailyHolder(private val binding: DailyItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DailyItem?) {
            binding.mDailyModel = item
            binding.executePendingBindings()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyHolder {
        return DailyHolder(DailyItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: DailyHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    companion object DiffCallBack : DiffUtil.ItemCallback<DailyItem>() {
        override fun areItemsTheSame(oldItem: DailyItem, newItem: DailyItem) = oldItem == newItem
        override fun areContentsTheSame(oldItem: DailyItem, newItem: DailyItem) =
            oldItem.dt == newItem.dt
    }


}
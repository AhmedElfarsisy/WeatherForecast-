package com.iti.elfarsisy.mad41.myapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.iti.elfarsisy.mad41.myapplication.data.model.DailyItem
import com.iti.elfarsisy.mad41.myapplication.data.model.HourlyItem
import com.iti.elfarsisy.mad41.myapplication.databinding.HourlyItemBinding
import timber.log.Timber

class TodayAdapter : ListAdapter<HourlyItem, TodayAdapter.TodayHolder>(DiffCallBack) {
    class TodayHolder(private val binding: HourlyItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: HourlyItem?) {
            Timber.i("$item")
            binding.mHourlyModel = item
            binding.executePendingBindings()
        }

    }

    companion object DiffCallBack : DiffUtil.ItemCallback<HourlyItem>() {
        override fun areItemsTheSame(oldItem: HourlyItem, newItem: HourlyItem) = oldItem == newItem
        override fun areContentsTheSame(oldItem: HourlyItem, newItem: HourlyItem) =
            oldItem.dt == newItem.dt
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodayHolder {
        return TodayHolder(HourlyItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: TodayHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

}
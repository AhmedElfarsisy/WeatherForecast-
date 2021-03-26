package com.iti.elfarsisy.mad41.myapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.iti.elfarsisy.mad41.myapplication.data.model.WeatherAlertsLocal
import com.iti.elfarsisy.mad41.myapplication.databinding.AlertItemBinding
import com.iti.elfarsisy.mad41.myapplication.ui.alerts.OnAlertsClickListener
import timber.log.Timber

class WeatherAlertsAdapter :
    ListAdapter<WeatherAlertsLocal, WeatherAlertsAdapter.AlertsHolder>(DiffCallBack) {
    private lateinit var onAlertsClickListner: OnAlertsClickListener

    class AlertsHolder(private val binding: AlertItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var deleteImageView: ImageView = binding.deleteImageView

        fun bind(item: WeatherAlertsLocal?) {
            binding.mAlertsModel = item
            binding.executePendingBindings()
        }
    }
    fun setOnClick(onAlertsClickListener: OnAlertsClickListener) {
        this.onAlertsClickListner = onAlertsClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlertsHolder {
        return AlertsHolder(AlertItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: AlertsHolder, position: Int) {
        val item = getItem(position)

        holder.deleteImageView.setOnClickListener {
            item.end?.let { it1 -> onAlertsClickListner.onDeleteAlert(it1) }
        }
        holder.bind(item)
    }
    companion object DiffCallBack : DiffUtil.ItemCallback<WeatherAlertsLocal>() {
        override fun areItemsTheSame(oldItem: WeatherAlertsLocal, newItem: WeatherAlertsLocal) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: WeatherAlertsLocal, newItem: WeatherAlertsLocal) =
            oldItem.end == newItem.end
    }
}
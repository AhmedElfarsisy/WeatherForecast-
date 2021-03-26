package com.iti.elfarsisy.mad41.myapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.iti.elfarsisy.mad41.myapplication.data.model.SavedPlaces
import com.iti.elfarsisy.mad41.myapplication.databinding.FavoriteItemBinding
import com.iti.elfarsisy.mad41.myapplication.ui.favorite.OnFavoriteClickListner
import timber.log.Timber

class FavoritePlacesAdapter() :
    ListAdapter<SavedPlaces, FavoritePlacesAdapter.FavoritePlacesHolder>(DiffCallBack) {
    private lateinit var onFavoriteClickListner: OnFavoriteClickListner

    class FavoritePlacesHolder(private val binding: FavoriteItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var deleteImageView: ImageView = binding.deleteImageView

        fun bind(item: SavedPlaces?) {
            Timber.i("$item")
            binding.mSavedPlaces = item
            binding.executePendingBindings()
        }

    }

    fun setOnClick(onFavoriteClickListner: OnFavoriteClickListner) {
        this.onFavoriteClickListner = onFavoriteClickListner
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritePlacesHolder {
        return FavoritePlacesHolder(FavoriteItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: FavoritePlacesHolder, position: Int) {
        val item = getItem(position)
        holder.itemView.setOnClickListener {
            onFavoriteClickListner.onPlaceClick(item.pId!!)
        }
        holder.deleteImageView.setOnClickListener {
            onFavoriteClickListner.onDeletePlace(item.pId!!)
        }
        holder.bind(item)
    }


    companion object DiffCallBack : DiffUtil.ItemCallback<SavedPlaces>() {
        override fun areItemsTheSame(oldItem: SavedPlaces, newItem: SavedPlaces) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: SavedPlaces, newItem: SavedPlaces) =
            oldItem.pId == newItem.pId
    }
}
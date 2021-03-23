package com.iti.elfarsisy.mad41.myapplication.ui.favoritedetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.iti.elfarsisy.mad41.myapplication.data.repo.IWeatherRepo
import com.iti.elfarsisy.mad41.myapplication.data.repo.UserSettingRepo

class FavoriteDetailsViewModelFactory(
    private val weatherRepo: IWeatherRepo,
    private val lat: Float,
    private val lon: Float,
    private val userSettingRepo: UserSettingRepo
) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        FavoriteDetailsViewModel(weatherRepo, lat, lon, userSettingRepo) as T
}
package com.iti.elfarsisy.mad41.myapplication.ui.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.iti.elfarsisy.mad41.myapplication.data.repo.ISavedPlacesRepo
import com.iti.elfarsisy.mad41.myapplication.data.repo.IWeatherRepo
import com.iti.elfarsisy.mad41.myapplication.data.repo.SavedPlacesRepo
import com.iti.elfarsisy.mad41.myapplication.data.repo.UserSettingRepo

class MapViewModelFactory(
    private val weatherRepo: IWeatherRepo,
    private val savedPlacesRepo: ISavedPlacesRepo,
    private val userSettingRepo: UserSettingRepo,
    private val screenId: Int
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        MapViewModel(weatherRepo, savedPlacesRepo, userSettingRepo, screenId) as T

}
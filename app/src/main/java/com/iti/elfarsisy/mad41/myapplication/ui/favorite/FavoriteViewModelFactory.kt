package com.iti.elfarsisy.mad41.myapplication.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.iti.elfarsisy.mad41.myapplication.data.repo.ISavedPlacesRepo
import com.iti.elfarsisy.mad41.myapplication.data.repo.IWeatherRepo
import com.iti.elfarsisy.mad41.myapplication.data.repo.UserSettingRepo

class FavoriteViewModelFactory(
    private val weatherRepo: IWeatherRepo,
    private val savedPlacesRepo: ISavedPlacesRepo,
    private val userSettingRepo: UserSettingRepo
) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        FavoriteViewModel(weatherRepo, savedPlacesRepo, userSettingRepo) as T
}
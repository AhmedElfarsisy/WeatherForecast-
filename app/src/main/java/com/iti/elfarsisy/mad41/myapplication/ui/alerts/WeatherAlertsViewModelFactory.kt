package com.iti.elfarsisy.mad41.myapplication.ui.alerts

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.iti.elfarsisy.mad41.myapplication.data.repo.IWeatherRepo
import com.iti.elfarsisy.mad41.myapplication.data.repo.SavedPlacesRepo
import com.iti.elfarsisy.mad41.myapplication.data.repo.UserSettingRepo

class WeatherAlertsViewModelFactory(
    private val weatherRepo: IWeatherRepo,
    private val alertsRepo: SavedPlacesRepo,
    private val userSettingRepo: UserSettingRepo,
    private val requireActivity: FragmentActivity
) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        WeatherAlertsViewModel(weatherRepo, alertsRepo, userSettingRepo, requireActivity) as T
}
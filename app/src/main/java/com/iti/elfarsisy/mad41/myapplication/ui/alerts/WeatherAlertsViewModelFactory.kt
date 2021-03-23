package com.iti.elfarsisy.mad41.myapplication.ui.alerts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.iti.elfarsisy.mad41.myapplication.data.repo.IWeatherRepo
import com.iti.elfarsisy.mad41.myapplication.data.repo.UserSettingRepo

class WeatherAlertsViewModelFactory(
    private val weatherRepo: IWeatherRepo,
    private val userSettingRepo: UserSettingRepo
) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        WeatherAlertsViewModel(weatherRepo, userSettingRepo) as T
}
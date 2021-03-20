package com.iti.elfarsisy.mad41.myapplication.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.iti.elfarsisy.mad41.myapplication.data.repo.IWeatherRepo
import com.iti.elfarsisy.mad41.myapplication.data.repo.UserSettingRepo
import java.lang.IllegalArgumentException

class HomeViewModelFactory(private val weatherRepo: IWeatherRepo,private val userSettingRepo: UserSettingRepo) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        HomeViewModel(weatherRepo,userSettingRepo) as T
}
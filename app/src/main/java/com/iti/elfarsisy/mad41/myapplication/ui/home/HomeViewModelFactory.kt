package com.iti.elfarsisy.mad41.myapplication.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.iti.elfarsisy.mad41.myapplication.data.repo.IWeatherRepo
import java.lang.IllegalArgumentException

class HomeViewModelFactory(private val weatherRepo: IWeatherRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            HomeViewModel(weatherRepo) as T
        } else {
            throw (IllegalArgumentException("HomeViewModel Not Found"))
        }
    }
}
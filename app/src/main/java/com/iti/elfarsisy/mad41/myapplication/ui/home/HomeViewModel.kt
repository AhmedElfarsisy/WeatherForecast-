package com.iti.elfarsisy.mad41.myapplication.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.iti.elfarsisy.mad41.myapplication.data.model.WeatherData
import com.iti.elfarsisy.mad41.myapplication.data.repo.IWeatherRepo
import com.iti.elfarsisy.mad41.myapplication.data.source.remote.NetworkState
import com.iti.elfarsisy.mad41.myapplication.helper.APP_ID
import com.iti.elfarsisy.mad41.myapplication.helper.UNITS_METRIC
import kotlinx.coroutines.*
import timber.log.Timber

class HomeViewModel(private val weatherRepo: IWeatherRepo) : ViewModel() {

    private val _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState> = _networkState
    private val _WeatherResponseLive = MutableLiveData<WeatherData>()
    val weatherResponseLive: LiveData<WeatherData> = _WeatherResponseLive


    init {
        _networkState.postValue(NetworkState.LOADING)
        fetchWeatherData()
    }

    private fun fetchWeatherData() {

        CoroutineScope(Dispatchers.IO).launch {
            val weatherResponse =
                async {
                    weatherRepo.fetchWeatherData(
                        30.00,
                        31.00,
                        UNITS_METRIC,
                        lang = "en",
                        APP_ID
                    )
                }
            if (weatherResponse.await().isSuccessful) {
                withContext(Dispatchers.Main) {
                    _WeatherResponseLive.postValue(weatherResponse.await().body())
                }
            } else {
                Timber.i("fetchWeatherData: Data not available")
            }

        }
    }

}
package com.iti.elfarsisy.mad41.myapplication.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.iti.elfarsisy.mad41.myapplication.data.model.DailyItem
import com.iti.elfarsisy.mad41.myapplication.data.model.HourlyItem
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
    private val _weatherResponseLive = MutableLiveData<WeatherData>()
    val weatherResponseLive: LiveData<WeatherData> = _weatherResponseLive
    private val _dailyResponseLive = MutableLiveData<List<DailyItem?>?>()
    val dailyResponseLive: LiveData<List<DailyItem?>?> = _dailyResponseLive
    private val _hourlyResponseLive = MutableLiveData<List<HourlyItem?>?>()
    val hourlyResponseLive: LiveData<List<HourlyItem?>?> = _hourlyResponseLive

    var coroutineExceptionHandler =
        CoroutineExceptionHandler { _, _ ->
            updataNetworkState(NetworkState.ERROR)
        }


    init {
        updataNetworkState(NetworkState.LOADING)
        fetchWeatherData()
    }

    private fun fetchWeatherData() {

        CoroutineScope(Dispatchers.IO + coroutineExceptionHandler).launch {
            val weatherResponse =
                async {
                    weatherRepo.fetchWeatherData(30.00, 31.00, UNITS_METRIC, lang = "en", APP_ID)
                }

            withContext(Dispatchers.Main) {
                updataNetworkState(NetworkState.LOADING)
                if (weatherResponse.await().isSuccessful) {
                    Timber.i("Start")
                    _weatherResponseLive.value = weatherResponse.await().body()
                    _dailyResponseLive.value = weatherResponse.await().body()?.daily
                    _hourlyResponseLive.value = weatherResponse.await().body()?.hourly
                    Timber.i("fetchWeatherData: Done")

                    updataNetworkState(NetworkState.DONE)
                } else {
                    updataNetworkState(NetworkState.ERROR)
                    _dailyResponseLive.postValue(ArrayList())
                    _hourlyResponseLive.postValue(ArrayList())
                    Timber.i("fetchWeatherData: Data not available")

                }
            }

        }
    }

    private fun updataNetworkState(state: NetworkState) {
        _networkState.postValue(state)
    }

}
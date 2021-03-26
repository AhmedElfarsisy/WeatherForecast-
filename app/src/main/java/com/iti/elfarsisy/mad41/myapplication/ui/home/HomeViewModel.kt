package com.iti.elfarsisy.mad41.myapplication.ui.home

import android.location.Address
import android.location.Geocoder
import androidx.lifecycle.*
import com.iti.elfarsisy.mad41.myapplication.data.model.DailyItem
import com.iti.elfarsisy.mad41.myapplication.data.model.HourlyItem
import com.iti.elfarsisy.mad41.myapplication.data.model.WeatherData
import com.iti.elfarsisy.mad41.myapplication.data.repo.IWeatherRepo
import com.iti.elfarsisy.mad41.myapplication.data.repo.UserSettingRepo
import com.iti.elfarsisy.mad41.myapplication.data.source.remote.NetworkState
import com.iti.elfarsisy.mad41.myapplication.helper.*
import com.iti.elfarsisy.mad41.myapplication.util.MyApplication
import kotlinx.coroutines.*
import timber.log.Timber
import java.io.IOException

class HomeViewModel(
    private val weatherRepo: IWeatherRepo,
    private val userSettingRepo: UserSettingRepo
) : ViewModel() {

    private val _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState> = _networkState
    private val _weatherResponseLive = MediatorLiveData<WeatherData>()
    val weatherResponseLive: MediatorLiveData<WeatherData> = _weatherResponseLive
    private val _dailyResponseLive = MediatorLiveData<MutableList<DailyItem>>()
    val dailyResponseLive: MediatorLiveData<MutableList<DailyItem>> = _dailyResponseLive
    private val _hourlyResponseLive = MediatorLiveData<List<HourlyItem?>?>()
    val hourlyResponseLive: MediatorLiveData<List<HourlyItem?>?> = _hourlyResponseLive
    private val _cityLive = MutableLiveData<String>()
    val cityLive: LiveData<String> = _cityLive
    private val _locationToolLive = MutableLiveData<String>()
    val locationToolLive: LiveData<String> = _locationToolLive

    private lateinit var units: String;
    private lateinit var lang: String;


    init {
        updateNetworkState(NetworkState.LOADING)
        readUserSettings()
    }

    private fun readUserSettings() {
        decideUnit(userSettingRepo.read(TEMP_MEASUREMENT_KEY, TEMP_CELSIUS_VALUES)!!)
        _locationToolLive.value = userSettingRepo.read(LOCATION_tool_KEY, GPS_LOCATION_VALUES)!!
        lang = userSettingRepo.read(APP_LOCAL_KEY, APP_LOCAL_EN_VALUES)!!
    }

    private fun decideUnit(unit: String) {
        when (unit) {
            TEMP_CELSIUS_VALUES -> units = UNITS_METRIC
            TEMP_KELVIN_VALUES -> units = UNITS_STANDERD
            TEMP_FAHRENHEIT_VALUES -> units = UNITS_IMPERIAL
            else -> units = UNITS_METRIC
        }
    }

    private fun fetchWeatherData(lat: Double, lon: Double) {
        viewModelScope.launch {
            val weatherResponse =
                async { weatherRepo.fetchWeatherData(lat, lon, lang = lang, units) }
            getLocationDescription(lat, lon)
            if (weatherResponse.await().isSuccessful) {
                _weatherResponseLive.postValue(weatherResponse.await().body())
                _dailyResponseLive.postValue(weatherResponse.await().body()?.daily)
                _hourlyResponseLive.postValue(weatherResponse.await().body()?.hourly)
                withContext(Dispatchers.IO) {
                    weatherResponse.await().body()?.let {
                        insertWeatherDataToLocal(it)
                    }
                }
                updateNetworkState(NetworkState.DONE)
                Timber.i("Successful API $lat, $lon")
            } else {
                updateNetworkState(NetworkState.ERROR)
                fetchWeatherDataFromLocal(lat, lon)
                Timber.i("${weatherResponse.await().message()}")
            }
        }
    }

    private fun fetchWeatherDataFromLocal(lat: Double, lon: Double) {
        weatherRepo.getWeatherDataById(lat)?.let { liveWeather ->
            weatherResponseLive.addSource(liveWeather) {
                weatherResponseLive.postValue(it)
                dailyResponseLive.postValue(it.daily)
                hourlyResponseLive.postValue(it.hourly)
            }
        }
    }

    private fun updateNetworkState(state: NetworkState) {
        _networkState.postValue(state)
    }

    fun setLatAndLong(latitude: Double, longitude: Double) {
        val locationDescription = getLocationDescription(latitude, longitude)
        fetchWeatherData(latitude, longitude)
        _cityLive.postValue(locationDescription?.subAdminArea)
    }

    private fun insertWeatherDataToLocal(weatherData: WeatherData) {
        CoroutineScope(Dispatchers.IO).launch {
            weatherRepo.insertWeatherData(weatherData)
        }
    }
}


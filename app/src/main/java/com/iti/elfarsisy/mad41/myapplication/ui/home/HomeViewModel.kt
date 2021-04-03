package com.iti.elfarsisy.mad41.myapplication.ui.home

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
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

class HomeViewModel(
    private val weatherRepo: IWeatherRepo,
    private val userSettingRepo: UserSettingRepo
) : ViewModel() {

    //region Properties
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
    private lateinit var units: String
    private lateinit var lang: String
    private val _isOnlineLive = MutableLiveData<Boolean>()
    val isOnlineLive: LiveData<Boolean> = _isOnlineLive
// endregion

    init {
        updateNetworkState(NetworkState.LOADING)
        readUserSettings()
    }

    private fun readUserSettings() {
        decideUnit(userSettingRepo.read(TEMP_MEASUREMENT_KEY, TEMP_CELSIUS_VALUES)!!)
        _locationToolLive.value = userSettingRepo.read(LOCATION_tool_KEY, GPS_LOCATION_VALUES)!!
        lang = userSettingRepo.read(APP_LOCAL_KEY, APP_LOCAL_EN_VALUES)!!
        _locationToolLive.postValue(userSettingRepo.read(LOCATION_tool_KEY, GPS_LOCATION_VALUES))
    }

    private fun decideUnit(unit: String) {
        units = when (unit) {
            TEMP_CELSIUS_VALUES -> UNITS_METRIC
            TEMP_KELVIN_VALUES -> UNITS_STANDERD
            TEMP_FAHRENHEIT_VALUES -> UNITS_IMPERIAL
            else -> UNITS_METRIC
        }
    }

    fun fetchWeatherData(lat: Double, lon: Double) {
        if (isOnline(MyApplication.getContext())) {
            _isOnlineLive.postValue(false)
            fetchOnlineWeatherData(lat, lon)
        } else {
            _isOnlineLive.postValue(true)
            fetchWeatherDataFromLocal(lat, lon)
        }
    }

    private fun fetchOnlineWeatherData(lat: Double, lon: Double) {
        viewModelScope.launch {
            val weatherResponse =
                async { weatherRepo.fetchWeatherData(lat, lon, lang = lang, units) }
            weatherResponse.await()?.let { response ->
                if (response.isSuccessful) {
                    _weatherResponseLive.postValue(response.body())
                    _dailyResponseLive.postValue(response.body()?.daily)
                    _hourlyResponseLive.postValue(response.body()?.hourly)
                    withContext(Dispatchers.IO) {
                        response.body()?.let {
                            insertWeatherDataToLocal(it)
                        }
                    }
                    updateNetworkState(NetworkState.DONE)
                    Timber.i("Successful API $lat, $lon")
                } else {
                    updateNetworkState(NetworkState.ERROR)
                    Timber.i("${weatherResponse.await().message()}")
                }
            }

        }
    }

    private fun fetchWeatherDataFromLocal(latitude: Double, lon: Double) {
        val lat = String.format("%.4f", latitude).toDouble()
        weatherRepo.getWeatherDataById(lat)?.let { liveWeather ->
            weatherResponseLive.addSource(liveWeather) {
                dailyResponseLive.postValue(it.daily)
                hourlyResponseLive.postValue(it.hourly)
            }
            updateNetworkState(NetworkState.DONE)
        }
    }

    private fun updateNetworkState(state: NetworkState) {
        _networkState.postValue(state)
    }

    fun setLatAndLong(latitude: Double, longitude: Double) {
        userSettingRepo.write(LAT_KEY, latitude.toString())
        userSettingRepo.write(LON_KEY, longitude.toString())
        getLocation()
    }

    private fun insertWeatherDataToLocal(weatherData: WeatherData) {
        CoroutineScope(Dispatchers.IO).launch {
            weatherRepo.insertWeatherData(weatherData)
        }
    }

    fun getLocation() {
        val userLat = userSettingRepo.read(LAT_KEY, 0.0.toString())?.toDouble() ?: 0.0
        val userLon = userSettingRepo.read(LON_KEY, 0.0.toString())?.toDouble() ?: 0.0
        val locationDescription = getLocationDescription(userLat, userLon)
        _cityLive.postValue(locationDescription?.subAdminArea)
        fetchWeatherData(userLat, userLon)
    }


}


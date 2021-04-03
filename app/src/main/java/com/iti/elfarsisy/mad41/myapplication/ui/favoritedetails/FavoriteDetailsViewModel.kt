package com.iti.elfarsisy.mad41.myapplication.ui.favoritedetails

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
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

class FavoriteDetailsViewModel(
    private val weatherRepo: IWeatherRepo,
    private val lat: Float,
    private val lon: Float,
    private val userSettingRepo: UserSettingRepo
) :
    ViewModel() {
    //    region PROPERTIES
    private val _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState> = _networkState
    private val _weatherResponseLive = MediatorLiveData<WeatherData>()
    val weatherResponseLive: MediatorLiveData<WeatherData> = _weatherResponseLive
    private val _dailyResponseLive = MediatorLiveData<List<DailyItem?>?>()
    val dailyResponseLive: MediatorLiveData<List<DailyItem?>?> = _dailyResponseLive
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
//    endregion

    init {
        updateNetworkState(NetworkState.LOADING)

        readUserSettings()

        fetchWeatherData(lat.toDouble(), lon.toDouble())

        setLatAndLong(lat.toDouble(), lon.toDouble())
    }

    private fun readUserSettings() {
        decideUnit(userSettingRepo.read(TEMP_MEASUREMENT_KEY, TEMP_CELSIUS_VALUES)!!)
        _locationToolLive.value = userSettingRepo.read(LOCATION_tool_KEY, GPS_LOCATION_VALUES)!!
        lang = userSettingRepo.read(APP_LOCAL_KEY, APP_LOCAL_EN_VALUES)!!
        Timber.i("My Setting Langauge $lang")
    }

    private fun decideUnit(unit: String) {
        if (unit == TEMP_CELSIUS_VALUES) {
            units = UNITS_METRIC
        } else if (unit == TEMP_KELVIN_VALUES) {
            units = UNITS_STANDERD
        } else if (unit == TEMP_FAHRENHEIT_VALUES) {
            units = UNITS_IMPERIAL
        } else {
            units = UNITS_METRIC
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

    private fun fetchWeatherDataFromLocal(lat: Double, lon: Double) {
        weatherRepo.getWeatherDataById(lat)?.let { liveWeather ->
            weatherResponseLive.addSource(liveWeather) {
                weatherResponseLive.postValue(it)
                Timber.i("$it")
                dailyResponseLive.postValue(it.daily)
                hourlyResponseLive.postValue(it.hourly)
            }
            updateNetworkState(NetworkState.DONE)
        }
    }

    private fun setLatAndLong(latitude: Double, longitude: Double) {
        val locationDescription = getLocationDescription(latitude, longitude)
        Timber.i("check lat lon  $latitude ,$longitude")
        _cityLive.postValue(locationDescription?.subAdminArea)
        Timber.i("Get Location Description ${cityLive.value}")

    }

    private fun insertWeatherDataToLocal(weatherData: WeatherData) {
        CoroutineScope(Dispatchers.IO).launch {
            weatherRepo.insertWeatherData(weatherData)
        }
    }

    private fun updateNetworkState(state: NetworkState) {
        _networkState.postValue(state)
    }


}
package com.iti.elfarsisy.mad41.myapplication.ui.home

import android.location.Address
import android.location.Geocoder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val _weatherResponseLive = MutableLiveData<WeatherData>()
    val weatherResponseLive: LiveData<WeatherData> = _weatherResponseLive
    private val _dailyResponseLive = MutableLiveData<List<DailyItem?>?>()
    val dailyResponseLive: LiveData<List<DailyItem?>?> = _dailyResponseLive
    private val _hourlyResponseLive = MutableLiveData<List<HourlyItem?>?>()
    val hourlyResponseLive: LiveData<List<HourlyItem?>?> = _hourlyResponseLive
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

    private fun fetchWeatherData(lat: Double, lon: Double) {
        viewModelScope.launch {
            val weatherResponse =
                async { weatherRepo.fetchWeatherData(lat, lon, lang = lang, units) }
            getLocationDescription(lat, lon)
            if (weatherResponse.await().isSuccessful) {
                _weatherResponseLive.postValue(weatherResponse.await().body())
                _dailyResponseLive.postValue(weatherResponse.await().body()?.daily)
                _hourlyResponseLive.postValue(weatherResponse.await().body()?.hourly)
                updateNetworkState(NetworkState.DONE)
                Timber.i("Successful API $lat, $lon")
            } else {
                updateNetworkState(NetworkState.ERROR)
                Timber.i("${weatherResponse.await().message()}")
            }
        }
    }


    private fun updateNetworkState(state: NetworkState) {
        _networkState.postValue(state)
    }

    fun setLatAndLong(latitude: Double, longitude: Double) {
        fetchWeatherData(latitude, longitude)
        Timber.i("set Lat Long ${cityLive.value}")
    }


    private fun getLocationDescription(latitude: Double, longitude: Double) {
        Timber.i("check lat lon  $latitude ,$longitude")
        val geocoder = Geocoder(MyApplication.getContext())
        try {
            val addresses: List<Address> = geocoder.getFromLocation(
                latitude,
                longitude,
                1
            )
            Timber.i("Get Location Description ${addresses[0].subAdminArea}")
            _cityLive.postValue(addresses[0].subAdminArea)
        } catch (e: IOException) {
            Timber.e(e.localizedMessage)
        }
    }
}


//
//    private fun fetchWeatherData(latitude: Double, longitude: Double) {
//        Timber.i("$latitude, $longitude")
////        Timber.i("LLLLLL ${getLocationDescription(latitude, longitude).toString()}")
//        CoroutineScope(Dispatchers.IO + coroutineExceptionHandler).launch {
//            val weatherResponse =
//                async {
//                    weatherRepo.fetchWeatherData( 37.4219983, -122.084, units, lang = lang, APP_ID)
//                }
//
//            withContext(Dispatchers.Main) {
//                updataNetworkState(NetworkState.LOADING)
//                if (weatherResponse.await().isSuccessful) {
//                    Timber.i("Start")
//                    _weatherResponseLive.value = weatherResponse.await().body()
//                    _dailyResponseLive.value = weatherResponse.await().body()?.daily
//                    _hourlyResponseLive.value = weatherResponse.await().body()?.hourly
////                    val address = getLocationDescription(latitude, longitude);
////                    _cityLive.value = address?.subAdminArea
////                    Timber.i("fetchWeatherData: Done")
//                    updataNetworkState(NetworkState.DONE)
//                } else {
//                    updataNetworkState(NetworkState.ERROR)
//                    _dailyResponseLive.postValue(ArrayList())
//                    _hourlyResponseLive.postValue(ArrayList())
//                    Timber.i("fetchWeatherData: Data not available")
//
//                }
//            }
//
//        }
//
//    }
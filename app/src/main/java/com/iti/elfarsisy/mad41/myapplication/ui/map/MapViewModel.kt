package com.iti.elfarsisy.mad41.myapplication.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.elfarsisy.mad41.myapplication.data.model.SavedPlaces
import com.iti.elfarsisy.mad41.myapplication.data.repo.ISavedPlacesRepo
import com.iti.elfarsisy.mad41.myapplication.data.repo.IWeatherRepo
import com.iti.elfarsisy.mad41.myapplication.data.repo.UserSettingRepo
import com.iti.elfarsisy.mad41.myapplication.helper.LAT_KEY
import com.iti.elfarsisy.mad41.myapplication.helper.LON_KEY
import com.iti.elfarsisy.mad41.myapplication.helper.getLocationDescription
import kotlinx.coroutines.launch

class MapViewModel(
    private val weatherRepo: IWeatherRepo,
    private val savedPlacesRepo: ISavedPlacesRepo,
    private val userSettingRepo: UserSettingRepo,
    val screenId: Int
) : ViewModel() {


    init {
    }

    fun setLocation(lat: Double, lon: Double) {
        _locationLan.postValue(lat)
        _locationLon.postValue(lon)
        getLocationInfo(lat, lon)
    }

    private fun getLocationInfo(lat: Double, lon: Double) {
        val address = getLocationDescription(lat, lon)
        _locationInfo.postValue("${address?.adminArea},${address?.subAdminArea}")
    }

    fun addLocationToFavourites() {
        if (locationLat.value == null) {
            _locationInfo.value = "please,pick location"
        } else {
            val lat = locationLat.value
            val lon = locationLon.value
            when (screenId) {
                0 -> {
                    //save in SharedPreferences
                    userSettingRepo.write(LAT_KEY, "$lat")
                    userSettingRepo.write(LON_KEY, "$lon")
                }
                1 -> {//save Favorites in DB
                    viewModelScope.launch {
                        savedPlacesRepo.insertFavoritePlace(
                            SavedPlaces(
                                lat = lat,
                                lon = lon,
                                screenId = 1
                            )
                        )
                        navigateBack()
                    }
                }

                2 -> {
                    viewModelScope.launch {
                        savedPlacesRepo.insertFavoritePlace(
                            SavedPlaces(
                                lat = locationLat.value,
                                lon = locationLon.value,
                                screenId = 2
                            )
                        )
                        navigateBack()
                    }
                }
            }
        }
    }


    fun navigateBack() {
        navigatorBack.value = true
    }

    fun completeNavigation() {
        navigatorBack.value = false
    }


    //MARK: properties
    private val _locationInfo = MutableLiveData<String>()
    val locationInfo: LiveData<String> = _locationInfo

    private val _locationLan = MutableLiveData<Double>()
    val locationLat: LiveData<Double> = _locationLan

    private val _locationLon = MutableLiveData<Double>()
    val locationLon: LiveData<Double> = _locationLon
    val navigatorBack = MutableLiveData<Boolean>()

}
package com.iti.elfarsisy.mad41.myapplication.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.iti.elfarsisy.mad41.myapplication.data.repo.UserSettingRepo
import com.iti.elfarsisy.mad41.myapplication.helper.*


class SettingsViewModel(private val userSettingRepo: UserSettingRepo) : ViewModel() {
    private val _navigatorToMap = MutableLiveData<Boolean>()
    val navigatorToMap: LiveData<Boolean> = _navigatorToMap

    private val _langLiveData = MutableLiveData<Pair<String, Boolean>>()
    val langLiveData: LiveData<Pair<String, Boolean>> = _langLiveData

//        Language.setLanguage(context = requireContext(), language = lang)
//                (activity as MainActivity).recreate()

    val locationMethod = MutableLiveData<String>()
    val language = MutableLiveData<String>()
    val tempMeasure = MutableLiveData<String>()
    val windMeasure = MutableLiveData<String>()

    init {
        readSettingsFromSharedPrefrances()
    }

    private fun readSettingsFromSharedPrefrances() {
        language.value = userSettingRepo.read(APP_LOCAL_KEY, APP_LOCAL_EN_VALUES)
        locationMethod.value = userSettingRepo.read(LOCATION_tool_KEY, GPS_LOCATION_VALUES)
        tempMeasure.value = userSettingRepo.read(TEMP_MEASUREMENT_KEY, TEMP_CELSIUS_VALUES)
        windMeasure.value =
            userSettingRepo.read(WIND_SPEED_MEASUREMENT_KEY, WIND_SPEED_METER_SEC_VALUES)
    }

    //Lang
    fun chooseArabic() {
        userSettingRepo.write(APP_LOCAL_KEY, APP_LOCAL_AR_VALUES)
        _langLiveData.value = Pair(APP_LOCAL_AR_VALUES, true)
        language.value = userSettingRepo.read(APP_LOCAL_KEY, APP_LOCAL_EN_VALUES)
    }

    fun chooseEnglish() {
        userSettingRepo.write(APP_LOCAL_KEY, APP_LOCAL_EN_VALUES)
        _langLiveData.value = Pair(APP_LOCAL_EN_VALUES, true)
        language.value = userSettingRepo.read(APP_LOCAL_KEY, APP_LOCAL_EN_VALUES)

    }

    //Location
    fun chooseLocationGPS() {
        userSettingRepo.write(LOCATION_tool_KEY, GPS_LOCATION_VALUES)
    }

    fun chooseLocationMap() {
        userSettingRepo.write(LOCATION_tool_KEY, MAP_LOCATION_VALUES)
        navigateToMap()
    }

    //TEMP
    fun chooseTempCELSIUS() {
        userSettingRepo.write(TEMP_MEASUREMENT_KEY, TEMP_CELSIUS_VALUES)
    }

    fun chooseTempKelvin() {
        userSettingRepo.write(TEMP_MEASUREMENT_KEY, TEMP_KELVIN_VALUES)
    }

    fun chooseTempFahrenheit() {
        userSettingRepo.write(TEMP_MEASUREMENT_KEY, TEMP_FAHRENHEIT_VALUES)
    }

    // wind speed
    fun chooseWindSpeedMeter() {
        userSettingRepo.write(WIND_SPEED_MEASUREMENT_KEY, WIND_SPEED_METER_SEC_VALUES)
    }

    fun chooseWindSpeedMile() {
        userSettingRepo.write(WIND_SPEED_MEASUREMENT_KEY, WIND_SPEED_MILE_HOURE_VALUES)
    }

    fun navigateToMap() {
        _navigatorToMap.value = true
    }

    fun completNavigation() {
        _navigatorToMap.value = false
    }

    fun completeLocalChange() {
        _langLiveData.value = Pair(APP_LOCAL_EN_VALUES, false)

    }

}
package com.iti.elfarsisy.mad41.myapplication.data.repo

import androidx.lifecycle.LiveData
import androidx.room.Query
import com.iti.elfarsisy.mad41.myapplication.data.model.SavedPlaces
import com.iti.elfarsisy.mad41.myapplication.data.model.WeatherData
import com.iti.elfarsisy.mad41.myapplication.helper.APP_ID
import com.iti.elfarsisy.mad41.myapplication.helper.APP_LOCAL_EN_VALUES
import com.iti.elfarsisy.mad41.myapplication.helper.UNITS_METRIC
import retrofit2.Response

interface IWeatherRepo {
    suspend fun fetchWeatherData(
        lan: Double,
        lon: Double,
        lang: String,
        units: String = UNITS_METRIC,
        appId: String = APP_ID
    ): Response<WeatherData>

    suspend fun fetchWeatherAlerts(
        lan: Double,
        lon: Double,
        appId: String = APP_ID
    ): Response<WeatherData>


    suspend fun insertWeatherData(weather: WeatherData)

    suspend fun deleteWeatherDataById(latId: Double)

    fun getWeatherDataById(latId: Double): LiveData<WeatherData>
}
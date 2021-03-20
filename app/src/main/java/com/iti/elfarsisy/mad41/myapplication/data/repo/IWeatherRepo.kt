package com.iti.elfarsisy.mad41.myapplication.data.repo

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
}
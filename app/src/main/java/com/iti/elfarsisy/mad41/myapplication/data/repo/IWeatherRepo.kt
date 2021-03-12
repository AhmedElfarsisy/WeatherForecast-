package com.iti.elfarsisy.mad41.myapplication.data.repo

import com.iti.elfarsisy.mad41.myapplication.data.model.WeatherData
import retrofit2.Response

interface IWeatherRepo {
    suspend fun fetchWeatherData(lan: Double, lon: Double, units: String, lang: String, appId: String): Response<WeatherData>
}
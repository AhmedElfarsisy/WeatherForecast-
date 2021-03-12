package com.iti.elfarsisy.mad41.myapplication.data.repo

import android.util.Log
import com.iti.elfarsisy.mad41.myapplication.data.model.WeatherData
import com.iti.elfarsisy.mad41.myapplication.data.source.remote.WeatherApi
import com.iti.elfarsisy.mad41.myapplication.helper.EXCLUDE_MINUTELY
import retrofit2.Response

class WeatherRepo : IWeatherRepo {

    override suspend fun fetchWeatherData(
        lan: Double,
        lon: Double,
        units: String,
        lang: String,
        appId: String
    ): Response<WeatherData> {
      return  WeatherApi.getWeatherRetrofitClient()
            .fetchWeatherData(lan, lon, EXCLUDE_MINUTELY, units, lang, appId)
    }


}
package com.iti.elfarsisy.mad41.myapplication.data.repo

import android.util.Log
import com.iti.elfarsisy.mad41.myapplication.data.model.WeatherData
import com.iti.elfarsisy.mad41.myapplication.data.source.remote.WeatherApi
import com.iti.elfarsisy.mad41.myapplication.helper.APP_ID
import com.iti.elfarsisy.mad41.myapplication.helper.EXCLUDE_MINUTELY
import retrofit2.Response

class WeatherRepo : IWeatherRepo {

    override suspend fun fetchWeatherData(
        lan: Double,
        lon: Double,
        lang: String,
        units: String,
        appId: String
    ): Response<WeatherData> {
        return WeatherApi.getWeatherRetrofitClient()
            .fetchWeatherData(lan, lon, lang, EXCLUDE_MINUTELY, units, appId)
    }


}
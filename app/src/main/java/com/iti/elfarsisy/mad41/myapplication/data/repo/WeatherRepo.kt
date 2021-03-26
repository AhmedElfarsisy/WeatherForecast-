package com.iti.elfarsisy.mad41.myapplication.data.repo

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.iti.elfarsisy.mad41.myapplication.data.model.WeatherData
import com.iti.elfarsisy.mad41.myapplication.data.source.local.WeatherDatabase
import com.iti.elfarsisy.mad41.myapplication.data.source.remote.WeatherApi
import com.iti.elfarsisy.mad41.myapplication.helper.APP_ID
import com.iti.elfarsisy.mad41.myapplication.helper.EXCLUDE_MINUTELY
import retrofit2.Response
import timber.log.Timber

class WeatherRepo(val application: Context) : IWeatherRepo {
    private val databaseRoom: WeatherDatabase = WeatherDatabase.getInstance(application)
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

    override suspend fun fetchWeatherAlerts(
        lan: Double,
        lon: Double,
        appId: String
    ): Response<WeatherData> {
        return WeatherApi.getWeatherRetrofitClient()
            .fetchWeatherAlerts(lat = lan, lon = lon, appID = appId)
    }


    override suspend fun insertWeatherData(weather: WeatherData) {
        databaseRoom.weatherDao().insert(weather)
    }

    override suspend fun deleteWeatherDataById(latId: Double) {
        Timber.i("@@@@@@ DeleteXXXXX weather Data From ModelView")
        databaseRoom.weatherDao().delete(latId)
    }

    override fun getWeatherDataById(latId: Double): LiveData<WeatherData> {
        return databaseRoom.weatherDao().getWeatherById(latId)
    }


}
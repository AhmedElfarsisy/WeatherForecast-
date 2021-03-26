package com.iti.elfarsisy.mad41.myapplication.data.source.remote

import com.iti.elfarsisy.mad41.myapplication.data.model.WeatherData
import com.iti.elfarsisy.mad41.myapplication.helper.EXCLUDE_MINUTELY
import com.iti.elfarsisy.mad41.myapplication.helper.UNITS_STANDERD
import com.iti.elfarsisy.mad41.myapplication.helper.WEATHER_BASE_URL
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit


object WeatherApi {
    var okHttpClient = OkHttpClient().newBuilder()
        .connectTimeout(25, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    fun getWeatherRetrofitClient() = Retrofit.Builder()
        .baseUrl(WEATHER_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build().create(WeatherApiServices::class.java)
}


interface WeatherApiServices {
    @GET("onecall")
    suspend fun fetchWeatherData(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("lang") lang: String = "en",
        @Query("exclude") exclude: String = EXCLUDE_MINUTELY,
        @Query("units") units: String = UNITS_STANDERD,
        @Query("appid") appID: String
    ): Response<WeatherData>

    suspend fun fetchWeatherAlerts(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("lang") lang: String = "en",
        @Query("exclude") exclude: String = "$EXCLUDE_MINUTELY,current" + ",daily" + ",minutely" + ",hourly",
        @Query("units") units: String = UNITS_STANDERD,
        @Query("appid") appID: String
    ): Response<WeatherData>
}
package com.iti.elfarsisy.mad41.myapplication.data.source.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.iti.elfarsisy.mad41.myapplication.data.model.WeatherData

@Dao
interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(weather: WeatherData)

    @Query("DELETE FROM weather_data WHERE lat=:latId")
    suspend fun delete(latId: Double)

    @Query("SELECT * FROM weather_data WHERE lat=:latId")
    fun getWeatherById(latId: Double): LiveData<WeatherData>?
}
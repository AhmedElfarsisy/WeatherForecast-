package com.iti.elfarsisy.mad41.myapplication.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import com.iti.elfarsisy.mad41.myapplication.data.model.WeatherData

@Dao
interface WeatherDao {
    @Insert
    suspend fun insert(weather: WeatherData)
}
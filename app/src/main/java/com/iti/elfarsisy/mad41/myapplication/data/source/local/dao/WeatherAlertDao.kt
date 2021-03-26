package com.iti.elfarsisy.mad41.myapplication.data.source.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.iti.elfarsisy.mad41.myapplication.data.model.SavedPlaces
import com.iti.elfarsisy.mad41.myapplication.data.model.WeatherAlertsLocal

@Dao
interface WeatherAlertDao {
    @Insert
    suspend fun insert(alert: WeatherAlertsLocal)

    @Query("DELETE FROM alerts WHERE  `end`=:id")
    suspend fun delete(id: Long)

    @Query("SELECT * FROM alerts  ORDER BY `end` DESC")
    fun getAllAlerts(): LiveData<MutableList<WeatherAlertsLocal>>

}
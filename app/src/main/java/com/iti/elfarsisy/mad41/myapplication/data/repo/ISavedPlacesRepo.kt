package com.iti.elfarsisy.mad41.myapplication.data.repo

import androidx.lifecycle.LiveData
import com.iti.elfarsisy.mad41.myapplication.data.model.SavedPlaces
import com.iti.elfarsisy.mad41.myapplication.data.model.WeatherAlertsLocal

interface ISavedPlacesRepo {
    fun getFavoritePlaces(): LiveData<MutableList<SavedPlaces>>

    suspend fun insertFavoritePlace(place: SavedPlaces)

    suspend fun deleteFavoritePlace(id: Int)

    fun getAllAlerts(): LiveData<MutableList<WeatherAlertsLocal>>

    suspend fun insertAlert(alert: WeatherAlertsLocal)

    suspend fun deleteAlert(id: Long)

    fun getPlaceById(id: Int): SavedPlaces

}
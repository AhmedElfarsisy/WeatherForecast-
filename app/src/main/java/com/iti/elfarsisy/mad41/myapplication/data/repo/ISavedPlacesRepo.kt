package com.iti.elfarsisy.mad41.myapplication.data.repo

import androidx.lifecycle.LiveData
import com.iti.elfarsisy.mad41.myapplication.data.model.SavedPlaces

interface ISavedPlacesRepo {
    suspend fun getFavoritePlaces(): List<SavedPlaces>

    suspend fun insertFavoritePlace(place: SavedPlaces)

    suspend fun deleteFavoritePlace(id: Int)

    suspend fun getAllAlertPlaces(): LiveData<List<SavedPlaces>>

    suspend fun insertAlertPlace(place: SavedPlaces)

    suspend fun deleteAlertPlace(id: Int)
    fun getPlaceById(id: Int): SavedPlaces
}
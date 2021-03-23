package com.iti.elfarsisy.mad41.myapplication.data.repo

import android.content.Context
import androidx.lifecycle.LiveData
import com.iti.elfarsisy.mad41.myapplication.data.model.SavedPlaces
import com.iti.elfarsisy.mad41.myapplication.data.source.local.WeatherDatabase


class SavedPlacesRepo(val application: Context) : ISavedPlacesRepo {
    private val databaseRoom: WeatherDatabase = WeatherDatabase.getInstance(application)
    override suspend fun getFavoritePlaces(): List<SavedPlaces> {
        return databaseRoom.favoritePlacesDao().getAllFavoritesPlaces()
    }

    override suspend fun insertFavoritePlace(place: SavedPlaces) {
        databaseRoom.favoritePlacesDao().insert(place)
    }

    override suspend fun deleteFavoritePlace(id: Int) {
        databaseRoom.favoritePlacesDao().delete(id)
    }

    override suspend fun getAllAlertPlaces(): LiveData<List<SavedPlaces>> {
        return databaseRoom.alertPlacesDao().getAllAlertPlaces()
    }

    override suspend fun insertAlertPlace(place: SavedPlaces) {
        databaseRoom.alertPlacesDao().insert(place)
    }

    override suspend fun deleteAlertPlace(id: Int) {
        databaseRoom.alertPlacesDao().delete(id)
    }

    override fun getPlaceById(id: Int):SavedPlaces {
       return databaseRoom.favoritePlacesDao().getPlaceById(id)
    }

}

//
//companion object {
//    @Volatile
//    private var INSTANCE: SavedPlacesRepo? = null
//    fun getINSTANCE(application: Context): SavedPlacesRepo {
//        WeatherDatabase.getInstance(application)
//        return INSTANCE ?: synchronized(this) {
//            val instance = SavedPlacesRepo(application)
//            INSTANCE = instance
//            instance
//        }
//
//    }
//
//}
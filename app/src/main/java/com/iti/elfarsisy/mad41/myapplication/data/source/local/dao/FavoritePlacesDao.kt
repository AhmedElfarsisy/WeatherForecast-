package com.iti.elfarsisy.mad41.myapplication.data.source.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.iti.elfarsisy.mad41.myapplication.data.model.SavedPlaces
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritePlacesDao {
    @Insert
    suspend fun insert(place: SavedPlaces)

    @Query("DELETE FROM places WHERE pId=:placeId")
    suspend fun delete(placeId: Int)

    @Query("SELECT * FROM places WHERE screenId=1 ORDER BY pId DESC")
     fun getAllFavoritesPlaces():LiveData<MutableList<SavedPlaces>>

    @Query("SELECT * FROM places WHERE pId=:placeId")
    fun getPlaceById(placeId: Int): SavedPlaces


}
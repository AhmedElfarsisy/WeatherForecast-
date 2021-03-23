package com.iti.elfarsisy.mad41.myapplication.data.source.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.iti.elfarsisy.mad41.myapplication.data.model.SavedPlaces

@Dao
interface SavedPlacesDao {
    @Insert
    suspend fun insert(place: SavedPlaces)

    @Query("DELETE FROM places WHERE pId=:placeId")
    suspend fun delete(placeId: Int)

    @Query("SELECT * FROM places WHERE screenId=2 ORDER BY pId DESC")
    fun getAllAlertPlaces(): LiveData<List<SavedPlaces>>

}
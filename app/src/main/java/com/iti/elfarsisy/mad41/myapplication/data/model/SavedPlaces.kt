package com.iti.elfarsisy.mad41.myapplication.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "places")
data class SavedPlaces(
    @PrimaryKey(autoGenerate = true)
    val pId: Int?=null,
    val lat: Double?,
    val lon: Double?,
    val screenId: Int?
)

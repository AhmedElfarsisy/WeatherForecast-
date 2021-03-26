package com.iti.elfarsisy.mad41.myapplication.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "alerts")
data class WeatherAlertsLocal(
    val start: Long,
    @PrimaryKey
    val end: Long
)

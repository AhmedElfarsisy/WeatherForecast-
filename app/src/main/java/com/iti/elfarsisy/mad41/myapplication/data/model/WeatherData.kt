package com.iti.elfarsisy.mad41.myapplication.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "weather_data")
data class WeatherData(
    @field:SerializedName("lat")
    @PrimaryKey
    val lat: Double,
    @field:SerializedName("current")
    val current: Current? = null,

    @field:SerializedName("timezone")
    val timezone: String,

    @field:SerializedName("timezone_offset")
    val timezoneOffset: Int,

    @field:SerializedName("daily")
    val daily: MutableList<DailyItem>,

    @field:SerializedName("lon")
    val lon: Double? = null,

    @field:SerializedName("hourly")
    val hourly: List<HourlyItem>,
    @field:SerializedName("minutely")
    val minutely: List<MinutelyItem>,
    @field:SerializedName("alerts")
    val alerts: MutableList<WeatherAlerts>
) : Parcelable
package com.iti.elfarsisy.mad41.myapplication.data.model

import android.os.Parcelable
import androidx.room.Ignore
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class WeatherAlerts(
    @SerializedName("sender_name")
    val sender_name: String?=null,
    @SerializedName("event")
    val event: String?=null,
    @SerializedName("start")
    val start: Int?=null,
    @SerializedName("end")
    val end: Int?=null,
    @SerializedName("description")
    val description: String?=null
): Parcelable
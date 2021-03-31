package com.iti.elfarsisy.mad41.myapplication.data.source.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.iti.elfarsisy.mad41.myapplication.data.model.*


class DTOConverter {

    @TypeConverter
    fun fromDailyObjToString(list: List<DailyItem?>?): String? {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun fromDailyJsonToList(gson: String): List<DailyItem?>? {

        val listType = object : TypeToken<List<DailyItem?>>() {}.type

        return Gson().fromJson(gson, listType)
    }

    // Hourly
    @TypeConverter
    fun fromHourlyObjToString(list: List<HourlyItem?>?): String? {
        return Gson().toJson(list)
    }


    @TypeConverter
    fun fromHourlyJsonToList(gson: String): List<HourlyItem?>? {

        val listType = object : TypeToken<List<HourlyItem?>>() {}.type

        return Gson().fromJson(gson, listType)
    }

    // Minutely
    @TypeConverter
    fun fromMinutelyObjToString(list: List<MinutelyItem?>?): String? {
        return Gson().toJson(list)
    }


    @TypeConverter
    fun fromMinutelyJsonToList(gson: String): List<MinutelyItem?>? {

        val listType = object : TypeToken<List<MinutelyItem?>>() {}.type

        return Gson().fromJson(gson, listType)
    }


    @TypeConverter
    fun fromAlertsObjToString(list: List<WeatherAlerts?>?): String? {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun fromAlertsJsonToList(gson: String): List<WeatherAlerts?>? {
        val listType = object : TypeToken<List<WeatherAlerts?>>() {}.type
        return Gson().fromJson(gson, listType)
    }

    @TypeConverter
    fun fromCurrentObjToString(current: Current?): String? {
        return Gson().toJson(current)
    }

    @TypeConverter
    fun fromStringToCurrentObj(currentStr: String?): Current? {

        return Gson().fromJson(currentStr, Current::class.java)
    }



}
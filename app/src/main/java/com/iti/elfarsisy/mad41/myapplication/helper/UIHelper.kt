package com.iti.elfarsisy.mad41.myapplication.helper

import android.location.Address
import android.location.Geocoder
import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.iti.elfarsisy.mad41.myapplication.data.model.WeatherAlerts
import com.iti.elfarsisy.mad41.myapplication.util.MyApplication
import timber.log.Timber
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

fun getLocationDescription(latitude: Double, longitude: Double): Address? {
    val geocoder = Geocoder(MyApplication.getContext())
    val addresses: List<Address> = geocoder.getFromLocation(
        latitude,
        longitude,
        1
    )
    if (addresses.isNotEmpty()) {
        return addresses[0]
    } else
        return Address(Locale("ar"))
}

fun parseTimeStampToDate(timeStamp: Long): String {
    var date = ""
    timeStamp?.let {
        val simpleDateFormat = SimpleDateFormat("dd-MM-yyy \nhh:mm")
        val netDate = Date(timeStamp)
        date = simpleDateFormat.format(netDate)
    }
    return date
}


fun objectToString(alerts: List<WeatherAlerts>): String {
    return Gson().toJson(alerts)
}

fun stringToObject(alertStr:String?):MutableList<WeatherAlerts>{
    val listType = object : TypeToken<MutableList<WeatherAlerts?>>() {}.type
    return Gson().fromJson(alertStr, listType)
}
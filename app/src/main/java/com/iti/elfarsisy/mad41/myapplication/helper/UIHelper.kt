package com.iti.elfarsisy.mad41.myapplication.helper

import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.iti.elfarsisy.mad41.myapplication.R
import com.iti.elfarsisy.mad41.myapplication.data.model.WeatherAlerts
import com.iti.elfarsisy.mad41.myapplication.util.MyApplication
import timber.log.Timber
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

fun getLocationDescription(latitude: Double, longitude: Double): Address? {
    val geocoder = Geocoder(MyApplication.getContext())
    var addresses: List<Address>? = null
    try {
        addresses = geocoder.getFromLocation(
            latitude,
            longitude,
            1
        )
    } catch (io: IOException) {
        Timber.i(io.localizedMessage)
    }
    if (addresses.isNullOrEmpty()) {
        return Address(Locale("ar"))
    } else
        return return addresses[0]
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

fun stringToObject(alertStr: String?): MutableList<WeatherAlerts> {
    val listType = object : TypeToken<MutableList<WeatherAlerts?>>() {}.type
    return Gson().fromJson(alertStr, listType)
}


fun showError(error: String, view: View) {
    Snackbar.make(view, error, Snackbar.LENGTH_INDEFINITE)
        .setBackgroundTint(view.resources.getColor(R.color.design_default_color_error))
        .setActionTextColor(view.resources.getColor(R.color.white))
        .show()
}


fun isOnline(context: Context?): Boolean {
    if (context == null) return false
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    return true
                }
            }
        }
    } else {
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
            return true
        }
    }

    return false
}

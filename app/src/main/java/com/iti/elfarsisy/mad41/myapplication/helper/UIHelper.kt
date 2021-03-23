package com.iti.elfarsisy.mad41.myapplication.helper

import android.location.Address
import android.location.Geocoder
import com.iti.elfarsisy.mad41.myapplication.util.MyApplication
import timber.log.Timber
import java.io.IOException
import java.util.*

fun getLocationDescription(latitude: Double, longitude: Double): Address? {
    val geocoder = Geocoder(MyApplication.getContext())
    val addresses: List<Address> = geocoder.getFromLocation(
        latitude,
        longitude,
        1
    )
    if (addresses.isNotEmpty()){
        return addresses[0]
    }else
    return Address(Locale("ar"))
}
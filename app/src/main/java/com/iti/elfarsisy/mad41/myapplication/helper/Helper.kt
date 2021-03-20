package com.iti.elfarsisy.mad41.myapplication.helper

import android.location.Address
import android.location.Geocoder
import com.iti.elfarsisy.mad41.myapplication.util.MyApplication
import timber.log.Timber
import java.io.IOException

//fun getLocationDescription(latitude: Double, longitude: Double): Address? {
//    var address: Address? = null
//    val geocoder = Geocoder(MyApplication.getContext())
//    try {
//
//        val addresses: List<Address> = geocoder.getFromLocation(
//            latitude,
//            longitude,
//            1
//        )
//        return addresses[0]
//    } catch (e: IOException) {
//        Timber.e(e.localizedMessage)
//        return address
//    }
//}
package com.iti.elfarsisy.mad41.myapplication.adapter

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.iti.elfarsisy.mad41.myapplication.helper.WEATHER_IMAGE_BASE_URL
import com.iti.elfarsisy.mad41.myapplication.data.source.remote.NetworkState
import com.squareup.picasso.Picasso

    //attach DegreeSymbol to temp degree
    @BindingAdapter("tempWithDegreeSymbol")
    fun attachDegreeSymbol(textView: TextView, temp: Double?) {

    }

    //Parse Timestamp to normal clock
    @BindingAdapter("timeParser")
    fun parseTimeStamp(textView: TextView, timeStamp: Int?) {

    }

    //bind weather Icon
    @BindingAdapter("weatherIcon")
    fun bindWeatherIcon(weatherImageView: ImageView, path: String?) {
        Picasso.get().load("$WEATHER_IMAGE_BASE_URL$path.png").into(weatherImageView)
    }

    //bind Wind speed
    @BindingAdapter("windSpeed")
    fun bindWindSpeed(textView: TextView, windSpeed: Double?) {
    }

    //bind visibility Distance
    @BindingAdapter("visibilityDistance")
    fun bindVisibility(textView: TextView, visibility: Int?) {
    }


    //bind Humidity in air
    @BindingAdapter("HumidityInAir")
    fun bindHumidity(textView: TextView, humidity: Int?) {
    }

    //parse just day name from Timestamp
    @BindingAdapter("dayParser")
    fun bindDay(textView: TextView, timeStamp: Int?) {

    }
@BindingAdapter("ApiStatus")
fun bindStatus(statusProgressBar: ProgressBar, status: NetworkState?) {
    when (status) {
        NetworkState.LOADING -> {
            statusProgressBar.visibility = View.VISIBLE
        }
        NetworkState.ERROR -> {
            statusProgressBar.visibility = View.GONE
        }
        NetworkState.DONE -> {
            statusProgressBar.visibility = View.GONE
        }
    }
}




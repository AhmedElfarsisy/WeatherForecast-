package com.iti.elfarsisy.mad41.myapplication.adapter

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iti.elfarsisy.mad41.myapplication.data.model.DailyItem
import com.iti.elfarsisy.mad41.myapplication.data.model.HourlyItem
import com.iti.elfarsisy.mad41.myapplication.data.model.SavedPlaces
import com.iti.elfarsisy.mad41.myapplication.data.repo.UserSettingRepo
import com.iti.elfarsisy.mad41.myapplication.data.source.remote.NetworkState
import com.iti.elfarsisy.mad41.myapplication.helper.*
import com.iti.elfarsisy.mad41.myapplication.util.MyApplication
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

//attach DegreeSymbol to temp degree
@BindingAdapter("tempWithDegreeSymbol")
fun attachDegreeSymbol(textView: TextView, temp: Double?) {
    val userSetting = UserSettingRepo(MyApplication.getContext())
    val readTemp = userSetting.read(TEMP_MEASUREMENT_KEY, TEMP_CELSIUS_VALUES)
    temp?.let {
        when (readTemp) {
            TEMP_CELSIUS_VALUES -> {
                textView.text = "${it.toInt()}\u2103"
            }
            TEMP_FAHRENHEIT_VALUES -> {
                textView.text = "${it.toInt()}\u2109"
            }
            TEMP_KELVIN_VALUES -> {
                textView.text = "${it.toInt()}\u212A"
            }
        }

    }

}

//Parse Timestamp to normal clock
@BindingAdapter("timeParser")
fun parseTimeStamp(textView: TextView, timeStamp: Int) {
    val simpleDateFormat = SimpleDateFormat("hh:mm a")
    val netDate = Date(timeStamp * 1000L)
    val date = simpleDateFormat.format(netDate)
    Timber.i("$date")
    textView.text = date.toString()

}

//bind weather Icon
@BindingAdapter("weatherIcon")
fun bindWeatherIcon(weatherImageView: ImageView, path: String?) {
    path?.let {
        Glide.with(MyApplication.getContext()).load("$WEATHER_IMAGE_BASE_URL$path@2x.png")
            .into(weatherImageView)
    }
}
//UNITS_METRIC
//to get Celsius and wind speed in meter/sec From API
//Imperial
//to get Fahrenheit and wind speed in miles/hour from API
//to get Kelvin and wind speed in meter/sec from API from API

//region bind Wind speed
@BindingAdapter("windSpeed")
fun bindWindSpeed(textView: TextView, windSpeed: Double?) {
    val userSetting = UserSettingRepo(MyApplication.getContext())
    val readTemp = userSetting.read(TEMP_MEASUREMENT_KEY, TEMP_CELSIUS_VALUES)
    val readWindSpeed = userSetting.read(WIND_SPEED_MEASUREMENT_KEY, WIND_SPEED_METER_SEC_VALUES)

    windSpeed?.let {
        //temp CELSIUS
        if (readTemp == TEMP_CELSIUS_VALUES) {
            if (readWindSpeed == WIND_SPEED_METER_SEC_VALUES) {
                textView.text = "${windSpeed}\nm/sec"
            } else {
                textView.text = "${String.format("%.3f", it.times(0.4))}\nmile/hr"

            }
        } else if (readTemp == TEMP_FAHRENHEIT_VALUES || readTemp == TEMP_KELVIN_VALUES) {
            if (readWindSpeed == WIND_SPEED_MILE_HOURE_VALUES) {
                textView.text = "${windSpeed}\nmile/h"
            } else {
                textView.text = "${windSpeed?.times(2.2)}\nm/sec"
            }
        }
    }
}
//endregion

//bind visibility Distance
@BindingAdapter("visibilityDistance")
fun bindVisibility(textView: TextView, visibility: Int?) {
    visibility?.let {
        textView.text = "${visibility?.div(1000)}km"
    }
}


//bind Humidity in air
@BindingAdapter("HumidityInAir")
fun bindHumidity(textView: TextView, humidity: Int?) {
    humidity?.let {
        textView.text = "$humidity%"
    }
}

//parse just day name from Timestamp
@BindingAdapter("dayParser")
fun bindDay(textView: TextView, timeStamp: Int) {
//    "dd-MM-yyy ,hh:mm",
    val sdf = SimpleDateFormat("EEEE")
    val netDate = Date(timeStamp * 1000L)
    val date = sdf.format(netDate)
    textView.text = date.subSequence(0, 3)
}

@BindingAdapter("fullDateParser")
fun bindDate(textView: TextView, timeStamp: Int) {
    timeStamp?.let {
        val simpleDateFormat = SimpleDateFormat("EEEE,dd-MM")
        val netDate = Date(timeStamp * 1000L)
        val date = simpleDateFormat.format(netDate)
        textView.text = date
    }
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


@BindingAdapter("hourlyDataset")
fun setTodayRecycler(recyclerView: RecyclerView, dataList: List<HourlyItem>?) {
    val adapter = TodayAdapter()
    recyclerView.adapter = adapter
    adapter.submitList(dataList)
}

@BindingAdapter("dailyDataset")
fun setupDailyRecycler(recyclerView: RecyclerView, dataList: List<DailyItem>?) {
    val adapter = DailyAdapter()
    recyclerView.adapter = adapter
    adapter.submitList(dataList)
}


@BindingAdapter("favoriteDataset")
fun setupFavoriteRecycler(recyclerView: RecyclerView, dataList: List<SavedPlaces>?) {
    val adapter = recyclerView.adapter as FavoritePlacesAdapter
    adapter.submitList(dataList)

}

@BindingAdapter(value = ["latNum", "lonNum"], requireAll = true)
fun bindLocationInfo(textView: TextView, lat: Double, lon: Double) {
    val locationDescription = getLocationDescription(lat, lat)
    if (locationDescription?.subAdminArea.isNullOrEmpty()) {
        textView.text = "GeoCoder can not get this place info "
    } else {
        textView.text = locationDescription?.subAdminArea
    }

}


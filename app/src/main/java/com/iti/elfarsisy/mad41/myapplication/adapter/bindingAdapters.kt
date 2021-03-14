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
import com.iti.elfarsisy.mad41.myapplication.data.source.remote.NetworkState
import com.iti.elfarsisy.mad41.myapplication.helper.WEATHER_IMAGE_BASE_URL
import com.iti.elfarsisy.mad41.myapplication.util.MyApplication
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

//attach DegreeSymbol to temp degree
@BindingAdapter("tempWithDegreeSymbol")
fun attachDegreeSymbol(textView: TextView, temp: Double?) {
    temp?.let {
        val toString = it.toInt().toString()
        textView.text = "$toString \u2103"
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
    Glide.with(MyApplication.getContext()).load("$WEATHER_IMAGE_BASE_URL$path.png")
        .into(weatherImageView)
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
fun bindDay(textView: TextView, timeStamp: Int) {
//    "dd-MM-yyy ,hh:mm",
    val sdf = SimpleDateFormat("EEEE")
    val netDate = Date(timeStamp * 1000L)
    val date = sdf.format(netDate)
    textView.text = date.subSequence(0, 3)
}

@BindingAdapter("fullDateParser")
fun bindDate(textView: TextView, timeStamp: Int) {
    val simpleDateFormat = SimpleDateFormat("EEEE,dd-MM")
    val netDate = Date(timeStamp * 1000L)
    val date = simpleDateFormat.format(netDate)
    textView.text = date
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

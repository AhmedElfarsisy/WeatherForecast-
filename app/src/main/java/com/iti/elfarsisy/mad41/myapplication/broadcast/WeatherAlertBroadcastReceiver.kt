package com.iti.elfarsisy.mad41.myapplication.broadcast

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.iti.elfarsisy.mad41.myapplication.MainActivity
import com.iti.elfarsisy.mad41.myapplication.R
import com.iti.elfarsisy.mad41.myapplication.data.model.WeatherAlerts
import com.iti.elfarsisy.mad41.myapplication.data.model.WeatherData
import com.iti.elfarsisy.mad41.myapplication.data.repo.SavedPlacesRepo
import com.iti.elfarsisy.mad41.myapplication.data.repo.UserSettingRepo
import com.iti.elfarsisy.mad41.myapplication.data.repo.WeatherRepo
import com.iti.elfarsisy.mad41.myapplication.helper.LAT_KEY
import com.iti.elfarsisy.mad41.myapplication.helper.LON_KEY
import com.iti.elfarsisy.mad41.myapplication.services.WeatherAlertService
import com.iti.elfarsisy.mad41.myapplication.util.MyApplication
import kotlinx.coroutines.*
import retrofit2.Response
import timber.log.Timber


class WeatherAlertBroadcastReceiver : BroadcastReceiver() {
    val placeRepo = SavedPlacesRepo(MyApplication.getContext())
    lateinit var fetchWeatherAlerts: Deferred<Response<WeatherData>>
    private val weatherAlertsWeatherRepo = WeatherRepo(MyApplication.getContext())
    var alerts: MutableList<WeatherAlerts> = ArrayList<WeatherAlerts>()
    val userSettingRepo = UserSettingRepo(MyApplication.getContext())
    private lateinit var intentService: Intent

    override fun onReceive(context: Context, intent: Intent) {
        Timber.i("onReceive ####### lets enter coroutineScope")
        val lat = userSettingRepo.read(LAT_KEY, "" + 0.0)?.toDouble()
        val lon = userSettingRepo.read(LON_KEY, "" + 0.0)?.toDouble()
        Timber.i("onReceive ####### my location $lat   $lon")
        intentService = Intent(context, WeatherAlertService::class.java)
        //region make alert API Call
        CoroutineScope(Dispatchers.IO).launch {
            fetchWeatherAlerts =
                async { weatherAlertsWeatherRepo.fetchWeatherAlerts(lat!!, lon!!) }
            Timber.i("onReceive ####### await after call  alerts")
            fetchWeatherAlerts.await()?.let { response ->
                if (response.isSuccessful) {
                    Timber.i("onReceive ####### response succeeded")
                    response.body()?.alerts?.let { weatherAlertsList ->
                        alerts = weatherAlertsList
                        Timber.i("onReceive ####### My Alerts $alerts")
                    }
                    withContext(Dispatchers.Main) {
                        if (alerts.isNullOrEmpty()) {
                            Timber.i("onReceive ####### no no no alerts")
                            showNotification("Weather is fine", context)
                        } else {
                            alerts.get(alerts.lastIndex).description?.let {
                                Timber.i("onReceive ####### back with alerts")
                                intentService.putExtra("description", it)
                                showNotification(it, context)
                            }
                        }
                        Timber.i("onReceive ####### lets startAlarmService")
                        startAlarmService(context, intent)
                    }
                }
            }
        }
        //endregion

        //region delete alert from DB
        CoroutineScope(Dispatchers.IO).launch {
            intent?.let {
                val id = intent.getLongExtra("id", 0)
                placeRepo.deleteAlert(id)
            }
        }
        //endregion

        //region notification show and Alarm alert from DB

        //endregion
    }

    private fun startAlarmService(context: Context, intent: Intent) {
        intentService.putExtra("id", intent.getLongExtra("id", 0))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intentService)
        } else {
            context.startService(intentService)
        }
    }


    private fun showNotification(notificationMessage: String, context: Context) {
        lateinit var notificationManager: NotificationManager
        val CHANNEL_ID = "100"
        val notificationIntent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0)
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            val channelName = "weather"
            val imporatnce = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, channelName, imporatnce)
            channel.description = notificationMessage
            notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
            val builder: NotificationCompat.Builder =
                NotificationCompat.Builder(context, CHANNEL_ID)
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.ic_humidity)
                    .setContentTitle("Weather forecast")
                    .setContentText(notificationMessage)
                    .setContentIntent(pendingIntent)
                    .setFullScreenIntent(pendingIntent, true)
            Timber.i("onReceive ####### notify now")

            notificationManager.notify(1, builder.build())
        }
    }


}
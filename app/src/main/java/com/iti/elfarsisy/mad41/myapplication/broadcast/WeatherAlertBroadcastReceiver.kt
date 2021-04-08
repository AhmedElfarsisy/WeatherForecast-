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
import com.iti.elfarsisy.mad41.myapplication.helper.*
import com.iti.elfarsisy.mad41.myapplication.services.WeatherAlertService
import com.iti.elfarsisy.mad41.myapplication.util.MyApplication
import kotlinx.coroutines.*
import retrofit2.Response
import timber.log.Timber


class WeatherAlertBroadcastReceiver : BroadcastReceiver() {
    //region Properties
    val placeRepo = SavedPlacesRepo(MyApplication.getContext())
    lateinit var fetchWeatherAlerts: Deferred<Response<WeatherData>>
    private val weatherAlertsWeatherRepo = WeatherRepo(MyApplication.getContext())
    var alerts: MutableList<WeatherAlerts> = ArrayList<WeatherAlerts>()
    val userSettingRepo = UserSettingRepo(MyApplication.getContext())
    private lateinit var intentService: Intent
    private lateinit var alertTool: String
    private var lat: Double = 0.0
    private var lon: Double = 0.0

    //    endregion

    override fun onReceive(context: Context, intent: Intent) {
        readUserSetting()
        intentService = Intent(context, WeatherAlertService::class.java)

        //region make alert API Call
        if (isOnline(context)) {
            CoroutineScope(Dispatchers.IO).launch {
                fetchWeatherAlerts =
                    async { weatherAlertsWeatherRepo.fetchWeatherAlerts(lat, lon) }
                fetchWeatherAlerts.await().let { response ->
                    if (response.isSuccessful) {
                        response.body()?.alerts?.filter { !it.description.isNullOrEmpty() }?.take(1)
                            ?.let {
                                alerts.add(it[0])
                            }
                        //region  Alert Tool Notification or Alert -Dialog & Sound
                        withContext(Dispatchers.Main) {
                            prepareWeatherConditionNotification(context, intent)
                        }
                        //endregion

                        if (alertTool == ALERT_ALARM_VALUE) {
                            alerts.get(0).description?.let {
                                intentService.putExtra("description", it)
                                startAlarmService(context, intent)
                            }
                        }
                    }
                }
            }
        }
        //endregion

        deleteAlertFromDatabase(intent)
    }

    private fun readUserSetting() {
        alertTool = userSettingRepo.read(ALERT_TOOL_KEY, ALERT_NOTIFICATION_VALUE)
            ?: ALERT_NOTIFICATION_VALUE
        lat = userSettingRepo.read(LAT_KEY, 0.0.toString())?.toDouble() ?: 0.0
        lon = userSettingRepo.read(LON_KEY, 0.0.toString())?.toDouble() ?: 0.0
    }

    //region  Alert Tool Notification or Alert -Dialog & Sound
    private fun prepareWeatherConditionNotification(context: Context, intent: Intent) {
        if (alertTool == ALERT_NOTIFICATION_VALUE) {
            if (alerts.isNullOrEmpty()) {
                showNotification("Weather is fine", context)
            } else {
                alerts.get(0).description?.let {
                    Timber.i("onReceive ######## showWeatherAlertToUser ")
                    intentService.putExtra("description", it)
                    showNotification(it, context)
                }
            }
        }
    }
    //endregion

    //region delete alert from DB
    private fun deleteAlertFromDatabase(intent: Intent) {
        CoroutineScope(Dispatchers.IO).launch {
            intent?.let {
                val id = intent.getLongExtra("id", 0)
                placeRepo.deleteAlert(id)
            }
        }
    }
    //endregion

    //region startAlarmService
    private fun startAlarmService(context: Context, intent: Intent) {
        intentService.putExtra("id", intent.getLongExtra("id", 0))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intentService)
        } else {
            context.startService(intentService)
        }
    }
    //endregion

    //region showNotification
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
                    .setSmallIcon(R.drawable.ic_weather)
                    .setContentTitle("Weather forecast")
                    .setContentText(notificationMessage)
                    .setContentIntent(pendingIntent)
                    .setFullScreenIntent(pendingIntent, true)
            Timber.i("onReceive ####### notify now")

            notificationManager.notify(1, builder.build())
        }
    }
    //endregion

}
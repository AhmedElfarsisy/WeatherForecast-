package com.iti.elfarsisy.mad41.myapplication.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.iti.elfarsisy.mad41.myapplication.MainActivity
import com.iti.elfarsisy.mad41.myapplication.R
import timber.log.Timber


class WeatherAlertService : Service() {
    private lateinit var notificationManager: NotificationManager
    private val CHANNEL_ID = "100"

    override fun onCreate() {
        super.onCreate()
        showNotification("Welcome to hell")
        Timber.i("*************Service onCreate  on Alert **************")
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForegroundService(intent)
        Timber.i("*************Service onStartCommand  on Alert **************")
        showNotification("Welcome to hell")
        return START_STICKY
    }

    private fun showNotification(notificationTitle: String) {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            val channelName = "weather"
            val imporatnce = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, channelName, imporatnce)
            channel.description = "weather is fine"
            notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
            val builder: NotificationCompat.Builder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_humidity)
                .setContentTitle(notificationTitle)
                .setContentText("Go you can go out")
                .setContentIntent(pendingIntent)
                .setFullScreenIntent(pendingIntent, true)
            startForeground(1, builder.build())
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
    }
}
package com.iti.elfarsisy.mad41.myapplication.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.iti.elfarsisy.mad41.myapplication.MainActivity
import com.iti.elfarsisy.mad41.myapplication.R


class WeatherAlertService : Service() {
    private lateinit var mWindowManager: WindowManager
    private lateinit var mDialogView: View
    private lateinit var notificationManager: NotificationManager
    private val CHANNEL_ID = "100"
    lateinit var weatherDescTV: TextView;
    lateinit var mediaPlayer: MediaPlayer
    override fun onCreate() {
        super.onCreate()
        mDialogView = LayoutInflater.from(this).inflate(R.layout.custom_alert_dialog, null)
        //Add the view to the window.
        //Add the view to the window.
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )
        //Specify the view position
        params.gravity =
            Gravity.CENTER_VERTICAL or Gravity.CENTER_HORIZONTAL //Initially view will be added to center

        params.x = 100
        params.y = 100
        //Add the view to the window
        mWindowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        mWindowManager?.let { it.addView(mDialogView, params) }
        val dismiss: Button = mDialogView.findViewById(R.id.dismiss_btn)
        weatherDescTV = mDialogView.findViewById(R.id.`weatherِ_alert_textView`)

        dismiss.setOnClickListener {
            stopSelf()
        }


        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        mediaPlayer = MediaPlayer.create(this, R.raw.alarmsound)
        mediaPlayer.isLooping = true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        var weatherDesc: String = "weather is fine"
        if (intent != null) {
            if (intent.hasExtra("description")) {
                weatherDesc = intent.getStringExtra("description").toString()
//                intent?.let { weatherDesc = it.getStringExtra("description").toString() }
            }
        }
        weatherDescTV.text = weatherDesc
        showNotification("weather forecast")
        mediaPlayer.start()
        return START_STICKY
    }

    private fun showNotification(notificationTitle: String) {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            val channelName = "weather"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, channelName, importance)
            channel.description = "weather is fine"
            notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
            val builder: NotificationCompat.Builder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_humidity)
                .setContentTitle(notificationTitle)
                .setContentText("weather notification")
                .setContentIntent(pendingIntent)
                .setFullScreenIntent(pendingIntent, true)
            startForeground(2, builder.build())
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.stop()
        mDialogView?.let { mWindowManager?.removeView(mDialogView) }
    }
}
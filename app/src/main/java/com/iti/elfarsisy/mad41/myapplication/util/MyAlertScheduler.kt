package com.iti.elfarsisy.mad41.myapplication.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.iti.elfarsisy.mad41.myapplication.broadcast.WeatherAlertBroadcastReceiver
import timber.log.Timber

class MyAlertScheduler {
    fun schedule(context: Context, timestamp: Long) {
        Timber.i("onScheduleFirst: onClick ")
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, WeatherAlertBroadcastReceiver::class.java)
        intent.putExtra("id", timestamp)
        val pendingIntent = PendingIntent.getBroadcast(context, timestamp.toInt(), intent, 0)
        Timber.i("====: ==== after BroadCast  ")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Timber.i("====:before ====  setExact  ")
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, timestamp, pendingIntent)
            Timber.i("====: ==== after setExact  ")
        }
    }

    fun cancelAlarm(context: Context, alarmID: Long) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, WeatherAlertBroadcastReceiver::class.java)
        val alarmPendingIntent = PendingIntent.getBroadcast(context, alarmID.toInt(), intent, 0)
        alarmManager.cancel(alarmPendingIntent)
    }
}
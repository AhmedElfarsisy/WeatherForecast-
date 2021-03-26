package com.iti.elfarsisy.mad41.myapplication.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.iti.elfarsisy.mad41.myapplication.data.model.SavedPlaces
import com.iti.elfarsisy.mad41.myapplication.data.repo.SavedPlacesRepo
import com.iti.elfarsisy.mad41.myapplication.services.WeatherAlertService
import com.iti.elfarsisy.mad41.myapplication.util.MyApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber


class WeatherAlertBroadcastReceiver : BroadcastReceiver() {
    val placesRepo = SavedPlacesRepo(MyApplication.getContext())
    override fun onReceive(context: Context, intent: Intent) {
        if (Intent.ACTION_BOOT_COMPLETED == intent.action) {
        } else {
            startAlarmService(context, intent)
            CoroutineScope(Dispatchers.IO).launch {
                placesRepo.deleteAlert(intent.getLongExtra("id", 0))
            }
        }
    }

    private fun startAlarmService(context: Context, intent: Intent) {
        val intentService = Intent(context, WeatherAlertService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intentService)
        } else {
            context.startService(intentService)
        }
    }
}
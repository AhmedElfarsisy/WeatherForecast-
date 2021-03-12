package com.iti.elfarsisy.mad41.myapplication.util

import android.app.Application
import android.content.Context
import com.iti.elfarsisy.mad41.myapplication.BuildConfig
import timber.log.Timber
import timber.log.Timber.DebugTree

/*
* Class MyApplication
* config Timber
* Get Application context
* */
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        INSTANCE = applicationContext
        //plant timber
        if (BuildConfig.DEBUG) Timber.plant(DebugTree())
        Timber.i("Hello Application Class#########")
    }

    // contains function to return context
    companion object {
        private lateinit var INSTANCE: Context
        fun getContext() = INSTANCE
    }

}
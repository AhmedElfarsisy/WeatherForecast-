package com.iti.elfarsisy.mad41.myapplication.util

import android.app.Application
import android.content.Context
import com.iti.elfarsisy.mad41.myapplication.BuildConfig
import com.iti.elfarsisy.mad41.myapplication.data.repo.UserSettingRepo
import com.iti.elfarsisy.mad41.myapplication.helper.APP_LOCAL_AR_VALUES
import com.iti.elfarsisy.mad41.myapplication.helper.APP_LOCAL_EN_VALUES
import com.iti.elfarsisy.mad41.myapplication.helper.APP_LOCAL_KEY
import com.iti.elfarsisy.mad41.myapplication.helper.LocaleHelper
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
        setLocale()
        //plant timber
        if (BuildConfig.DEBUG) Timber.plant(DebugTree())
        Timber.i("Hello Application Class#########")
    }

    private fun setLocale() {
        val userSettingRepo = UserSettingRepo(applicationContext)
        val appLocale = userSettingRepo.read(APP_LOCAL_KEY, APP_LOCAL_AR_VALUES)
        LocaleHelper.setLocale(applicationContext, appLocale)
    }

    // contains function to return context
    companion object {
        private lateinit var INSTANCE: Context
        fun getContext() = INSTANCE
    }


}
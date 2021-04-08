package com.iti.elfarsisy.mad41.myapplication.util

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import com.iti.elfarsisy.mad41.myapplication.BuildConfig
import com.iti.elfarsisy.mad41.myapplication.data.repo.UserSettingRepo
import com.iti.elfarsisy.mad41.myapplication.helper.APP_LOCAL_AR_VALUES
import com.iti.elfarsisy.mad41.myapplication.helper.APP_LOCAL_EN_VALUES
import com.iti.elfarsisy.mad41.myapplication.helper.APP_LOCAL_KEY
import timber.log.Timber
import timber.log.Timber.DebugTree
import java.util.*


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

        val userSettingRepo = UserSettingRepo(applicationContext)
        val lang = userSettingRepo.read(APP_LOCAL_KEY, APP_LOCAL_EN_VALUES)
        Language.setLanguage(applicationContext, lang)
    }

    // contains function to return context
    companion object {
        private lateinit var INSTANCE: Context
        fun getContext() = INSTANCE
    }
}
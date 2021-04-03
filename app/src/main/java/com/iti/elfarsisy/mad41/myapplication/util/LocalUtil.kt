package com.iti.elfarsisy.mad41.myapplication.util

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.LocaleList
import com.iti.elfarsisy.mad41.myapplication.data.repo.UserSettingRepo
import com.iti.elfarsisy.mad41.myapplication.helper.APP_LOCAL_AR_VALUES
import com.iti.elfarsisy.mad41.myapplication.helper.APP_LOCAL_KEY
import java.util.*

object Language {
    //ToDo Change App Context  in UserSettingRepo
    val userSettingRepo = UserSettingRepo(MyApplication.getContext())

    fun setLanguage(context: Context, language: String?) {
        // Save selected language
        // Update language
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }

    fun currentLanguageString(context: Context): String? {
        return getString(context, currentLanguage(context))
    }

    private fun getString(context: Context, identifier: String?): String? {
        val resourceId = context.resources.getIdentifier(identifier, "string", context.packageName)
        return context.resources.getString(resourceId)
    }

    fun currentLanguage(context: Context): String? {
        val appLangauge = userSettingRepo.read(APP_LOCAL_KEY, APP_LOCAL_AR_VALUES)
        setLanguage(context,appLangauge )
        return appLangauge
    }
}
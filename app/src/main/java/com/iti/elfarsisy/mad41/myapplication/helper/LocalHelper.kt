package com.iti.elfarsisy.mad41.myapplication.helper

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import java.util.*


/**
 *     fun setLocale(context: Context, language: String?): Context {

 *
 */

//object LocaleHelper {
//    private const val SELECTED_LANGUAGE = "Locale.Helper.Selected.Language"
//
//    // the method is used to set the language at runtime
//    fun setLocale(context: Context, language: String): Context {
//        persist(context, language)
//
//        // updating the language for devices above android nougat
//        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            updateResources(context, language)
//        } else updateResourcesLegacy(context, language)
//        // for devices having lower version of android os
//    }
//
//    private fun persist(context: Context, language: String) {
//        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
//        val editor = preferences.edit()
//        editor.putString(APP_LOCAL_KEY, language)
//        editor.apply()
//    }
//
//    // the method is used update the language of application by creating
//    // object of inbuilt Locale class and passing language argument to it
//    @TargetApi(Build.VERSION_CODES.N)
//    private fun updateResources(context: Context, language: String): Context {
//        val locale = Locale(language)
//        Locale.setDefault(locale)
//        val configuration = context.resources.configuration
//        configuration.setLocale(locale)
//        configuration.setLayoutDirection(locale)
//        return context.createConfigurationContext(configuration)
//    }
//
//    private fun updateResourcesLegacy(context: Context, language: String): Context {
//        val locale = Locale(language)
//        Locale.setDefault(locale)
//        val resources = context.resources
//        val configuration = resources.configuration
//        configuration.locale = locale
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            configuration.setLayoutDirection(locale)
//        }
//        resources.updateConfiguration(configuration, resources.displayMetrics)
//        return context
//    }
//
//
//}
//    @SuppressLint("RestrictedApi")
//    fun updateLocale(context: Context, localeToSwitchTo: Locale): ContextWrapper? {
//        var context = context
//        val resources: Resources = context.resources
//        val configuration: Configuration = resources.getConfiguration() // 1
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            val localeList = LocaleList(localeToSwitchTo) // 2
//            LocaleList.setDefault(localeList) // 3
//            configuration.setLocales(localeList) // 4
//        } else {
//            configuration.locale = localeToSwitchTo // 5
//        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
//            context = context.createConfigurationContext(configuration) // 6
//        } else {
//            resources.updateConfiguration(configuration, resources.getDisplayMetrics()) // 7
//        }
////        return ContextUtils(context) // 8
//    }

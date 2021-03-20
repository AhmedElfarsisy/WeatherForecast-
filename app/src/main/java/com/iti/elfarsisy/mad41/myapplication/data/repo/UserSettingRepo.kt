package com.iti.elfarsisy.mad41.myapplication.data.repo

import android.content.Context
import com.iti.elfarsisy.mad41.myapplication.helper.USER_SETTING_FILE

class UserSettingRepo(private val context: Context) {

    fun read(key: String, defaultValue: String): String? {
        val sharedPreferences =
            context.getSharedPreferences(USER_SETTING_FILE, Context.MODE_PRIVATE)
        return sharedPreferences.getString(key, defaultValue)
    }

    fun write(key: String, value: String) {
        val sharedPreferences =
            context.getSharedPreferences(USER_SETTING_FILE, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(key, value).commit()

    }

}
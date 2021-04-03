package com.iti.elfarsisy.mad41.myapplication.util


import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        Language.setLanguage(this, Language.currentLanguage(this))
        super.onCreate(savedInstanceState, persistentState)
    }
}
package com.iti.elfarsisy.mad41.myapplication.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.iti.elfarsisy.mad41.myapplication.data.repo.UserSettingRepo

class SettingViewModelFactory(private val userSettingRepo: UserSettingRepo) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        SettingsViewModel(userSettingRepo) as T}
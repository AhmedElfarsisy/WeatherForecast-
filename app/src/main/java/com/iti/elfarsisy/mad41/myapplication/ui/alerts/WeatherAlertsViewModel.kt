package com.iti.elfarsisy.mad41.myapplication.ui.alerts

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import com.iti.elfarsisy.mad41.myapplication.data.model.WeatherAlertsLocal
import com.iti.elfarsisy.mad41.myapplication.data.repo.IWeatherRepo
import com.iti.elfarsisy.mad41.myapplication.data.repo.SavedPlacesRepo
import com.iti.elfarsisy.mad41.myapplication.data.repo.UserSettingRepo
import com.iti.elfarsisy.mad41.myapplication.util.MyAlertScheduler
import com.iti.elfarsisy.mad41.myapplication.util.MyApplication
import kotlinx.coroutines.*
import timber.log.Timber

class WeatherAlertsViewModel(
    private val weatherRepo: IWeatherRepo,
    private val alertsRepo: SavedPlacesRepo,
   private val userSettingRepo: UserSettingRepo,
   private val requireActivity: FragmentActivity
) :
    ViewModel() {
    private val alertScheduler = MyAlertScheduler()
    private val _alertAddition = MutableLiveData<Boolean>()
    val alertAddition: LiveData<Boolean> = _alertAddition
    var alertsLocalLive = MediatorLiveData<MutableList<WeatherAlertsLocal>>()

    init {
        fetchAlertsFromLocal()
    }

    fun showAddAlertDialog() {
        _alertAddition.postValue(true)
    }

    private fun fetchAlertsFromLocal() {
        alertsRepo.getAllAlerts()?.let { alertsLive ->
            alertsLocalLive.addSource(alertsLive) {
                alertsLocalLive.postValue(it)
            }
        }
    }

    fun delete(alertId: Long) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                alertsRepo.deleteAlert(alertId)
            }
            alertScheduler.cancelAlarm(requireActivity, alarmID = alertId)
        }
    }

    fun insertAlert(start: Long, end: Long) {
        viewModelScope.launch {
            alertsRepo.insertAlert(WeatherAlertsLocal(start = start, end = end))
            alertScheduler.schedule(requireActivity, end)
        }
    }

    fun dismissAddAlertCompleted() {
        _alertAddition.postValue(false)
    }
}
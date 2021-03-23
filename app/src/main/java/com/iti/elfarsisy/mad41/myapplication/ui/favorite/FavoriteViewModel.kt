package com.iti.elfarsisy.mad41.myapplication.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.elfarsisy.mad41.myapplication.data.model.SavedPlaces
import com.iti.elfarsisy.mad41.myapplication.data.repo.ISavedPlacesRepo
import com.iti.elfarsisy.mad41.myapplication.data.repo.IWeatherRepo
import com.iti.elfarsisy.mad41.myapplication.data.repo.UserSettingRepo
import kotlinx.coroutines.*
import timber.log.Timber

class FavoriteViewModel(
    val weatherRepo: IWeatherRepo,
    private val savedPlacesRepo: ISavedPlacesRepo,
    userSettingRepo: UserSettingRepo
) :
    ViewModel() {

    init {
        Timber.i("Hello LocationBody")
        fetchPlacesFromLocal()
    }

    private fun fetchPlacesFromLocal() {
        viewModelScope.launch {
            var async: Deferred<List<SavedPlaces>>
            withContext(Dispatchers.IO) {
                async = async { savedPlacesRepo.getFavoritePlaces() }
            }
            if (!async.await().isNullOrEmpty()) {
                _savedPlacesLive.postValue(async.await())
                Timber.i("Hello Location ${async.await().get(0).lat}")
            } else {
                Timber.i("Hello Database is Empty")

            }
        }
    }


    fun navigateToMap() {
        _navigatorToMap.value = true
    }

    fun completeNavigation() {
        _navigatorToMap.value = false
    }

    fun completeNavigationToDetails() {
        _navigateToFavoriteDetail.value = Pair(null, null)
    }

    fun navigateToFavoritePlace(placeId: Int) {
        viewModelScope.launch {
            var place: Deferred<SavedPlaces>
            withContext(Dispatchers.IO) {

                place = async { savedPlacesRepo.getPlaceById(placeId) }
            }
            val lat = place.await().lat
            val lon = place.await().lon
            _navigateToFavoriteDetail.postValue(Pair(lat, lon))
        }

    }

    fun delete(placeId: Int) {
        viewModelScope.launch {
            Timber.i("VM delete")
            savedPlacesRepo.deleteFavoritePlace(placeId)
            fetchPlacesFromLocal()
        }
    }

    fun refershUI() {
        fetchPlacesFromLocal()
    }

    //PROPERTIES
    private val _delete = MutableLiveData<Boolean>()
    val delete: LiveData<Boolean> = _delete
    private val _navigateToFavoriteDetail = MutableLiveData<Pair<Double?, Double?>>()
    val navigateToFavoriteDetail: LiveData<Pair<Double?, Double?>> = _navigateToFavoriteDetail
    private val _navigatorToMap = MutableLiveData<Boolean>()
    val navigatorToMap: LiveData<Boolean> = _navigatorToMap

    private val _savedPlacesLive = MutableLiveData<
            List<SavedPlaces>?>()
    val savedPlacesLive: LiveData<
            List<SavedPlaces>?> = _savedPlacesLive
}
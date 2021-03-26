package com.iti.elfarsisy.mad41.myapplication.ui.favorite

import androidx.lifecycle.*
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
) : ViewModel() {
    var savedPlacesLive = MediatorLiveData<MutableList<SavedPlaces>>()

    init {
        Timber.i("Hello LocationBody")
        fetchPlacesFromLocal()
    }

    private fun fetchPlacesFromLocal() {
        savedPlacesRepo.getFavoritePlaces()?.let { savedPlaces ->
            savedPlacesLive.addSource(savedPlaces) {
                savedPlacesLive.postValue(it)
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
            var place: Deferred<SavedPlaces>
            withContext(Dispatchers.IO) {
                place = async { savedPlacesRepo.getPlaceById(placeId) }
            }
            place.await().lat?.let {
                Timber.i("@@@@@@ DeleteXXXXX weather Data From ModelView")
                weatherRepo.deleteWeatherDataById(it)
                savedPlacesRepo.deleteFavoritePlace(placeId)
            }
//            fetchPlacesFromLocal()
        }
    }

    //PROPERTIES
    private val _delete = MutableLiveData<Boolean>()
    val delete: LiveData<Boolean> = _delete
    private val _navigateToFavoriteDetail = MutableLiveData<Pair<Double?, Double?>>()
    val navigateToFavoriteDetail: LiveData<Pair<Double?, Double?>> = _navigateToFavoriteDetail
    private val _navigatorToMap = MutableLiveData<Boolean>()
    val navigatorToMap: LiveData<Boolean> = _navigatorToMap

}
package com.udacity.asteroidradar.viewmodels

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.ApiStatus
import com.udacity.asteroidradar.api.Network
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch

class AsteroidViewModel(application: Application) : AndroidViewModel(application) {
    private val database = getDatabase(application)
    private val asteroidRepository = AsteroidRepository(database)
    val asteroids: LiveData<List<Asteroid>> = asteroidRepository.asteroids

    private val _apiStatus = MutableLiveData<ApiStatus>()
    val showLoadingView: LiveData<Int>
        get() = _apiStatus.map {
            if (it == ApiStatus.LOADING) View.VISIBLE else View.GONE
        }

    private val _pictureOfDay = MutableLiveData<PictureOfDay?>()
    val pictureOfDay: LiveData<PictureOfDay?>
        get() = _pictureOfDay

    private val _navigateToSelectedAsteroid = MutableLiveData<Asteroid?>()
    val navigateToSelectedAsteroid: LiveData<Asteroid?>
        get() = _navigateToSelectedAsteroid

    fun displayAsteroidDetails(asteroid: Asteroid) {
        _navigateToSelectedAsteroid.value = asteroid
    }

    fun displayAsteroidDetailsComplete() {
        _navigateToSelectedAsteroid.value = null
    }

    init {
        viewModelScope.launch {
            loadPictureOfDay()
            _apiStatus.value = ApiStatus.LOADING
            refreshAsteroids()
            _apiStatus.value = ApiStatus.DONE
        }
    }

    private suspend fun refreshAsteroids() {
        asteroidRepository.refreshNearEarthObjects()
    }

    private suspend fun loadPictureOfDay() {
        try {
            _pictureOfDay.value = Network.asteroidService.getPictureOfDay()
        } catch (e: Exception) {
            _pictureOfDay.value = null
        }
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AsteroidViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AsteroidViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
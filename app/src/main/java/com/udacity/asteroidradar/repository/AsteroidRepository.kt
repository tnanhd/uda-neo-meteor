package com.udacity.asteroidradar.repository

import android.util.Log
import com.udacity.asteroidradar.api.Network
import com.udacity.asteroidradar.api.asDatabaseModel
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.getCurrentDateString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AsteroidRepository(private val database: AsteroidDatabase) {

    suspend fun getAsteroids(
        startDate: String = getCurrentDateString(),
        endDate: String? = null
    ): List<Asteroid> {
        return database.asteroidDao.getAsteroids(startDate, endDate).asDomainModel()
    }

    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            try {
                val response = Network.asteroidService.getSteroids()
                database.asteroidDao.insertAll(*response.asDatabaseModel())
            } catch (e: Exception) {
                Log.e("refreshAsteroids", e.toString())
            }
        }
    }

}
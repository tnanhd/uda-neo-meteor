package com.udacity.asteroidradar.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.udacity.asteroidradar.api.Network
import com.udacity.asteroidradar.api.asDatabaseModel
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.DatabaseAsteroid
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.domain.Asteroid
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AsteroidRepository(private val database: AsteroidDatabase) {
    private var databaseAsteroids = database.asteroidDao.getAsteroids()
    val asteroids: LiveData<List<Asteroid>> =
        databaseAsteroids.map(List<DatabaseAsteroid>::asDomainModel)

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
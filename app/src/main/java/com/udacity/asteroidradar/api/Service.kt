package com.udacity.asteroidradar.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

enum class ApiStatus { LOADING, ERROR, DONE }

interface AsteroidService {
    @GET("neo/rest/v1/feed?api_key=${Constants.API_KEY}")
    suspend fun getNearEarthObjects(
        @Query("start_date") startDate: String = "2024-08-27",
        @Query("end_date") endDate: String = "2024-08-27"
    ): NetworkAsteroidResponse

    @GET("planetary/apod?api_key=${Constants.API_KEY}")
    suspend fun getPictureOfDay(): PictureOfDay
}

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

object Network {
    private val retrofit = retrofit2.Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    val asteroidService = retrofit.create(AsteroidService::class.java)
}
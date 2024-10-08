package com.udacity.asteroidradar.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.udacity.asteroidradar.database.DatabaseAsteroid

@JsonClass(generateAdapter = true)
data class NetworkAsteroidResponse(
    @Json(name = "near_earth_objects")
    val asteroids: Map<String, List<NetworkAsteroid>>
)

@JsonClass(generateAdapter = true)
data class NetworkAsteroid(
    val id: String,
    val name: String,
    @Json(name = "absolute_magnitude_h")
    val absoluteMagnitude: Double,
    @Json(name = "estimated_diameter")
    val estimatedDiameter: EstimatedDiameter,
    @Json(name = "is_potentially_hazardous_asteroid")
    val isPotentiallyHazardous: Boolean,
    @Json(name = "close_approach_data")
    val closeApproachData: List<CloseApproachData>

) {
    @JsonClass(generateAdapter = true)
    data class EstimatedDiameter(
        @Json(name = "kilometers")
        val kilometers: EstimatedDiameterKilo
    ) {
        @JsonClass(generateAdapter = true)
        data class EstimatedDiameterKilo(
            @Json(name = "estimated_diameter_max")
            val estimatedDiameterMin: Double,
            @Json(name = "estimated_diameter_min")
            val estimatedDiameterMax: Double
        )
    }

    @JsonClass(generateAdapter = true)
    data class CloseApproachData(
        @Json(name = "close_approach_date")
        val closeApproachDate: String,

        @Json(name = "relative_velocity")
        val relativeVelocity: RelativeVelocity,

        @Json(name = "miss_distance")
        val missDistance: MissDistance
    ) {
        @JsonClass(generateAdapter = true)
        data class RelativeVelocity(
            @Json(name = "kilometers_per_second")
            val kilometersPerSecond: String
        )

        @JsonClass(generateAdapter = true)
        data class MissDistance(
            val astronomical: String
        )
    }
}

fun NetworkAsteroidResponse.asDatabaseModel(): Array<DatabaseAsteroid> {
    return asteroids
        .flatMap { it.value }
        .map {
            DatabaseAsteroid(
                id = it.id,
                name = it.name,
                absoluteMagnitude = it.absoluteMagnitude,
                estimatedDiameterMax = it.estimatedDiameter.kilometers.estimatedDiameterMax,
                isPotentiallyHazardous = it.isPotentiallyHazardous,
                closeApproachDate = it.closeApproachData[0].closeApproachDate,
                relativeVelocity = it.closeApproachData[0].relativeVelocity.kilometersPerSecond,
                missDistance = it.closeApproachData[0].missDistance.astronomical
            )
        }.toTypedArray()
}
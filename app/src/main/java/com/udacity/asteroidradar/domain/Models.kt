package com.udacity.asteroidradar.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Asteroid(
    val id: String,
    val name: String,
    val absoluteMagnitude: Double,
    val estimatedDiameter: Double,
    val isPotentiallyHazardous: Boolean,
    val closeApproachDate: String,
    val relativeVelocity: String,
    val distanceFromEarth: String
) : Parcelable
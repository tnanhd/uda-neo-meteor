package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import com.udacity.asteroidradar.getCurrentDateString

@Dao
interface AsteroidDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg nearEarthObjects: DatabaseAsteroid)

    @Query("SELECT * FROM databaseasteroid WHERE closeApproachDate >= :startDate ORDER BY closeApproachDate DESC")
    fun getAsteroids(startDate: String = getCurrentDateString()): LiveData<List<DatabaseAsteroid>>
}

@Database(entities = [DatabaseAsteroid::class], version = 1, exportSchema = false)
abstract class AsteroidDatabase : RoomDatabase() {
    abstract val asteroidDao: AsteroidDao
}

private lateinit var INSTANCE: AsteroidDatabase

fun getDatabase(context: Context): AsteroidDatabase {
    synchronized(AsteroidDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = databaseBuilder(
                context.applicationContext,
                AsteroidDatabase::class.java,
                "asteroids_database"
            )
                .fallbackToDestructiveMigrationFrom()
                .build()
        }
    }
    return INSTANCE
}
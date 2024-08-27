package com.udacity.asteroidradar

import android.app.Application
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class AsteroidRadarApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        delayWorkerInit()
    }
}

val applicationScope = CoroutineScope(Dispatchers.Default)

private fun delayWorkerInit() = applicationScope.launch {
    setupRefreshDataWork()
}

private fun setupRefreshDataWork() {
    val constraints = Constraints.Builder()
        .setRequiresCharging(true)
        .setRequiredNetworkType(NetworkType.UNMETERED)
        .build()

    val workRequest = PeriodicWorkRequestBuilder<AsteroidRefreshDataWorker>(1, TimeUnit.DAYS)
        .setConstraints(constraints)
        .build()

    WorkManager.getInstance().enqueueUniquePeriodicWork(
        AsteroidRefreshDataWorker.WORK_NAME,
        ExistingPeriodicWorkPolicy.KEEP,
        workRequest
    )
}
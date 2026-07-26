package com.josegonzalez.jetpackCrypto

import android.app.Application
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.josegonzalez.jetpackCrypto.ui.work.CryptoPriceRefreshWorker
import java.util.concurrent.TimeUnit

class JetpackCryptoApp : Application() {

    override fun onCreate() {
        super.onCreate()
        schedulePriceRefresh()
    }

    private fun schedulePriceRefresh() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val refreshRequest = PeriodicWorkRequestBuilder<CryptoPriceRefreshWorker>(
            15, TimeUnit.MINUTES
        )
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "crypto_price_refresh",
            ExistingPeriodicWorkPolicy.KEEP,
            refreshRequest
        )
    }
}
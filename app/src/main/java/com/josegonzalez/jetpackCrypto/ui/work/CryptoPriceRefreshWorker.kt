package com.josegonzalez.jetpackCrypto.ui.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.josegonzalez.jetpackCrypto.data.local.database.CryptoDatabase
import com.josegonzalez.jetpackCrypto.data.remote.api.RetrofitClient
import com.josegonzalez.jetpackCrypto.data.remote.mapper.toDomain
import com.josegonzalez.jetpackCrypto.data.local.mapper.toEntity

class CryptoPriceRefreshWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            val db = CryptoDatabase.getInstance(applicationContext)
            val api = RetrofitClient.apiService
            val coins = api.getCoins(page = 1, perPage = 50)
            val entities = coins.map { it.toDomain().toEntity(page = 1) }
            db.cryptoDao().insertAll(entities)
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
package com.josegonzalez.jetpackCrypto.data.repository

import com.josegonzalez.jetpackCrypto.data.remote.api.CryptoApiService
import com.josegonzalez.jetpackCrypto.data.remote.mapper.toDomain
import com.josegonzalez.jetpackCrypto.domain.model.Coin
import com.josegonzalez.jetpackCrypto.domain.repository.CryptoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CryptoRepositoryImpl(
    private val apiService: CryptoApiService
) : CryptoRepository {

    override suspend fun getCoins(page: Int, perPage: Int): List<Coin> =
        withContext(Dispatchers.IO) {
            apiService.getCoins(page = page, perPage = perPage).map { it.toDomain() }
        }

    override suspend fun getCoinDetail(coinId: String): Coin =
        withContext(Dispatchers.IO) {
            apiService.getCoinDetail(coinId).toDomain()
        }
}

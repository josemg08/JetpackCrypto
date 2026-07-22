package com.josegonzalez.jetpackCrypto.domain.repository

import com.josegonzalez.jetpackCrypto.domain.model.Coin

interface CryptoRepository {
    suspend fun getCoins(page: Int, perPage: Int = 20): List<Coin>
    suspend fun getCoinDetail(coinId: String): Coin
}

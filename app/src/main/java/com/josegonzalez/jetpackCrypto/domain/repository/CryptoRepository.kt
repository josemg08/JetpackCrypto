package com.josegonzalez.jetpackCrypto.domain.repository

import androidx.paging.PagingData
import com.josegonzalez.jetpackCrypto.domain.model.Coin
import kotlinx.coroutines.flow.Flow

interface CryptoRepository {
    fun getCoinsPaged(): Flow<PagingData<Coin>>
    fun getTopGainers(): Flow<List<Coin>>
    fun searchCoins(query: String): Flow<List<Coin>>
    suspend fun getCoinDetail(coinId: String): Coin
}
package com.josegonzalez.jetpackCrypto.fake

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import com.josegonzalez.jetpackCrypto.domain.model.Coin
import com.josegonzalez.jetpackCrypto.domain.repository.CryptoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeCryptoRepository(
    private val coins: List<Coin> = emptyList(),
    private val coinDetail: Coin = Coin(
        id = "bitcoin",
        symbol = "BTC",
        name = "Bitcoin",
        imageUrl = "",
        currentPriceUsd = 50000.0,
        marketCapRank = 1,
        priceChangePercentage24h = 2.0,
        high24h = 51000.0,
        low24h = 49000.0,
        lastUpdated = ""
    ),
    private val shouldThrow: Boolean = false
) : CryptoRepository {

    override fun getCoinsPaged(): Flow<PagingData<Coin>> {
        return flowOf(PagingData.from(coins))
    }

    override fun getTopGainers(): LiveData<List<Coin>> {
        return MutableLiveData(coins.take(10))
    }

    override suspend fun getCoinDetail(coinId: String): Coin {
        if (shouldThrow) throw Exception("Network error")
        return coinDetail
    }
}

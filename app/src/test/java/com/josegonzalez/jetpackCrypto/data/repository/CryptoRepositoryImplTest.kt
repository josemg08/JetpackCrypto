package com.josegonzalez.jetpackCrypto.data.repository

import com.josegonzalez.jetpackCrypto.data.remote.model.CoinDetailDto
import com.josegonzalez.jetpackCrypto.data.remote.model.CoinDto
import com.josegonzalez.jetpackCrypto.data.remote.model.ImageDto
import com.josegonzalez.jetpackCrypto.data.remote.model.MarketDataDto
import com.josegonzalez.jetpackCrypto.fake.FakeCryptoApiService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CryptoRepositoryImplTest {

    @Test
    fun `getCoins calls api and maps to domain`() = runTest {
        val dto = CoinDto(
            id = "bitcoin",
            symbol = "btc",
            name = "Bitcoin",
            image = "url",
            currentPrice = 50000.0,
            marketCapRank = 1,
            priceChangePercentage24h = 2.0,
            high24h = 51000.0,
            low24h = 49000.0,
            lastUpdated = "now"
        )
        val apiService = FakeCryptoApiService(coins = listOf(dto))
        val repository = CryptoRepositoryImpl(apiService)

        val result = repository.getCoins(page = 1)

        assertEquals(1, result.size)
        assertEquals("bitcoin", result[0].id)
        assertEquals("BTC", result[0].symbol)
    }

    @Test
    fun `getCoinDetail calls api and maps to domain`() = runTest {
        val dto = CoinDetailDto(
            id = "bitcoin",
            symbol = "btc",
            name = "Bitcoin",
            image = ImageDto(large = "url"),
            marketData = MarketDataDto(
                currentPrice = mapOf("usd" to 50000.0),
                marketCapRank = 1,
                priceChangePercentage24h = 2.0,
                high24h = mapOf("usd" to 51000.0),
                low24h = mapOf("usd" to 49000.0),
                lastUpdated = "now"
            )
        )
        val apiService = FakeCryptoApiService(coinDetail = dto)
        val repository = CryptoRepositoryImpl(apiService)

        val result = repository.getCoinDetail("bitcoin")

        assertEquals("bitcoin", result.id)
        assertEquals("BTC", result.symbol)
        assertEquals(50000.0, result.currentPriceUsd, 0.0)
    }
}

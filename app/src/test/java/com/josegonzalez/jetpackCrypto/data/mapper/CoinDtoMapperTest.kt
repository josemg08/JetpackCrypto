package com.josegonzalez.jetpackCrypto.data.mapper

import com.josegonzalez.jetpackCrypto.data.remote.mapper.toDomain
import com.josegonzalez.jetpackCrypto.data.remote.model.CoinDetailDto
import com.josegonzalez.jetpackCrypto.data.remote.model.CoinDto
import com.josegonzalez.jetpackCrypto.data.remote.model.ImageDto
import com.josegonzalez.jetpackCrypto.data.remote.model.MarketDataDto
import org.junit.Assert.assertEquals
import org.junit.Test

class CoinDtoMapperTest {

    @Test
    fun `coinDto toDomain maps all fields correctly`() {
        val dto = CoinDto(
            id = "bitcoin",
            symbol = "btc",
            name = "Bitcoin",
            image = "https://example.com/btc.png",
            currentPrice = 50000.0,
            marketCapRank = 1,
            priceChangePercentage24h = 2.5,
            high24h = 51000.0,
            low24h = 49000.0,
            lastUpdated = "2023-10-27T10:00:00Z"
        )

        val domain = dto.toDomain()

        assertEquals("bitcoin", domain.id)
        assertEquals("BTC", domain.symbol)
        assertEquals("Bitcoin", domain.name)
        assertEquals("https://example.com/btc.png", domain.imageUrl)
        assertEquals(50000.0, domain.currentPriceUsd, 0.0)
        assertEquals(1, domain.marketCapRank)
        assertEquals(2.5, domain.priceChangePercentage24h, 0.0)
        assertEquals(51000.0, domain.high24h, 0.0)
        assertEquals(49000.0, domain.low24h, 0.0)
        assertEquals("2023-10-27T10:00:00Z", domain.lastUpdated)
    }

    @Test
    fun `coinDetailDto toDomain maps all fields correctly`() {
        val dto = CoinDetailDto(
            id = "bitcoin",
            symbol = "btc",
            name = "Bitcoin",
            image = ImageDto(large = "https://example.com/btc_large.png"),
            marketData = MarketDataDto(
                currentPrice = mapOf("usd" to 50000.0),
                marketCapRank = 1,
                priceChangePercentage24h = 2.5,
                high24h = mapOf("usd" to 51000.0),
                low24h = mapOf("usd" to 49000.0),
                lastUpdated = "2023-10-27T10:00:00Z"
            )
        )

        val domain = dto.toDomain()

        assertEquals("bitcoin", domain.id)
        assertEquals("BTC", domain.symbol)
        assertEquals("Bitcoin", domain.name)
        assertEquals("https://example.com/btc_large.png", domain.imageUrl)
        assertEquals(50000.0, domain.currentPriceUsd, 0.0)
        assertEquals(1, domain.marketCapRank)
        assertEquals(2.5, domain.priceChangePercentage24h, 0.0)
        assertEquals(51000.0, domain.high24h, 0.0)
        assertEquals(49000.0, domain.low24h, 0.0)
        assertEquals("2023-10-27T10:00:00Z", domain.lastUpdated)
    }

    @Test
    fun `coinDto toDomain with null values maps to defaults`() {
        val dto = CoinDto(
            id = "bitcoin",
            symbol = "btc",
            name = "Bitcoin",
            image = "url",
            currentPrice = null,
            marketCapRank = null,
            priceChangePercentage24h = null,
            high24h = null,
            low24h = null,
            lastUpdated = null
        )

        val domain = dto.toDomain()

        assertEquals(0.0, domain.currentPriceUsd, 0.0)
        assertEquals(0, domain.marketCapRank)
        assertEquals(0.0, domain.priceChangePercentage24h, 0.0)
        assertEquals(0.0, domain.high24h, 0.0)
        assertEquals(0.0, domain.low24h, 0.0)
        assertEquals("", domain.lastUpdated)
    }

    @Test
    fun `coinDetailDto toDomain with null values maps to defaults`() {
        val dto = CoinDetailDto(
            id = "bitcoin",
            symbol = "btc",
            name = "Bitcoin",
            image = null,
            marketData = null
        )

        val domain = dto.toDomain()

        assertEquals("", domain.imageUrl)
        assertEquals(0.0, domain.currentPriceUsd, 0.0)
        assertEquals(0, domain.marketCapRank)
        assertEquals(0.0, domain.priceChangePercentage24h, 0.0)
        assertEquals(0.0, domain.high24h, 0.0)
        assertEquals(0.0, domain.low24h, 0.0)
        assertEquals("", domain.lastUpdated)
    }

    @Test
    fun `coinDetailDto toDomain with missing usd key maps to defaults`() {
        val dto = CoinDetailDto(
            id = "bitcoin",
            symbol = "btc",
            name = "Bitcoin",
            image = ImageDto(large = "url"),
            marketData = MarketDataDto(
                currentPrice = emptyMap(),
                marketCapRank = 1,
                priceChangePercentage24h = 2.5,
                high24h = emptyMap(),
                low24h = emptyMap(),
                lastUpdated = "now"
            )
        )

        val domain = dto.toDomain()

        assertEquals(0.0, domain.currentPriceUsd, 0.0)
        assertEquals(0.0, domain.high24h, 0.0)
        assertEquals(0.0, domain.low24h, 0.0)
    }
}

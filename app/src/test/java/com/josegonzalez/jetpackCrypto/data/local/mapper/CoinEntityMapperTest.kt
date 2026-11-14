package com.josegonzalez.jetpackCrypto.data.local.mapper

import com.josegonzalez.jetpackCrypto.data.local.entity.CoinEntity
import com.josegonzalez.jetpackCrypto.domain.model.Coin
import org.junit.Assert.assertEquals
import org.junit.Test

class CoinEntityMapperTest {

    @Test
    fun `coinEntity toDomain maps all fields correctly`() {
        val entity = CoinEntity(
            id = "bitcoin",
            symbol = "BTC",
            name = "Bitcoin",
            imageUrl = "url",
            currentPriceUsd = 50000.0,
            marketCapRank = 1,
            priceChangePercentage24h = 2.0,
            high24h = 51000.0,
            low24h = 49000.0,
            lastUpdated = "now",
            page = 1
        )

        val domain = entity.toDomain()

        assertEquals(entity.id, domain.id)
        assertEquals(entity.symbol, domain.symbol)
        assertEquals(entity.name, domain.name)
        assertEquals(entity.imageUrl, domain.imageUrl)
        assertEquals(entity.currentPriceUsd, domain.currentPriceUsd, 0.0)
        assertEquals(entity.marketCapRank, domain.marketCapRank)
        assertEquals(entity.priceChangePercentage24h, domain.priceChangePercentage24h, 0.0)
        assertEquals(entity.high24h, domain.high24h, 0.0)
        assertEquals(entity.low24h, domain.low24h, 0.0)
        assertEquals(entity.lastUpdated, domain.lastUpdated)
    }

    @Test
    fun `coin toEntity maps all fields correctly`() {
        val domain = Coin(
            id = "bitcoin",
            symbol = "BTC",
            name = "Bitcoin",
            imageUrl = "url",
            currentPriceUsd = 50000.0,
            marketCapRank = 1,
            priceChangePercentage24h = 2.0,
            high24h = 51000.0,
            low24h = 49000.0,
            lastUpdated = "now"
        )
        val page = 1

        val entity = domain.toEntity(page)

        assertEquals(domain.id, entity.id)
        assertEquals(domain.symbol, entity.symbol)
        assertEquals(domain.name, entity.name)
        assertEquals(domain.imageUrl, entity.imageUrl)
        assertEquals(domain.currentPriceUsd, entity.currentPriceUsd, 0.0)
        assertEquals(domain.marketCapRank, entity.marketCapRank)
        assertEquals(domain.priceChangePercentage24h, entity.priceChangePercentage24h, 0.0)
        assertEquals(domain.high24h, entity.high24h, 0.0)
        assertEquals(domain.low24h, entity.low24h, 0.0)
        assertEquals(domain.lastUpdated, entity.lastUpdated)
        assertEquals(page, entity.page)
    }
}

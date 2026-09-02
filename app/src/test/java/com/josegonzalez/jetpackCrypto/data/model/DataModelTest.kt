package com.josegonzalez.jetpackCrypto.data.model

import com.josegonzalez.jetpackCrypto.data.local.entity.CoinEntity
import com.josegonzalez.jetpackCrypto.data.remote.model.CoinDetailDto
import com.josegonzalez.jetpackCrypto.data.remote.model.CoinDto
import com.josegonzalez.jetpackCrypto.data.remote.model.ImageDto
import com.josegonzalez.jetpackCrypto.data.remote.model.MarketDataDto
import org.junit.Assert.assertEquals
import org.junit.Test

class DataModelTest {

    @Test
    fun `coinEntity properties are correctly set`() {
        val entity = CoinEntity("id", "sym", "name", "url", 1.0, 1, 1.0, 1.0, 1.0, "now", 1)
        assertEquals("id", entity.id)
        assertEquals(1, entity.page)
    }

    @Test
    fun `coinDto properties are correctly set`() {
        val dto = CoinDto("id", "sym", "name", "url", 1.0, 1, 1.0, 1.0, 1.0, "now")
        assertEquals("id", dto.id)
    }

    @Test
    fun `coinDetailDto and sub-models properties are correctly set`() {
        val image = ImageDto("large")
        val marketData = MarketDataDto(mapOf("usd" to 1.0), 1, 1.0, mapOf("usd" to 1.0), mapOf("usd" to 1.0), "now")
        val dto = CoinDetailDto("id", "sym", "name", image, marketData)
        
        assertEquals("id", dto.id)
        assertEquals("large", dto.image?.large)
        assertEquals(1.0, dto.marketData?.currentPrice?.get("usd"))
    }
}

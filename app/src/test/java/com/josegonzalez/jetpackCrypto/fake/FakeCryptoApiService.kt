package com.josegonzalez.jetpackCrypto.fake

import com.josegonzalez.jetpackCrypto.data.remote.api.CryptoApiService
import com.josegonzalez.jetpackCrypto.data.remote.model.CoinDetailDto
import com.josegonzalez.jetpackCrypto.data.remote.model.CoinDto

class FakeCryptoApiService(
    private val coins: List<CoinDto> = emptyList(),
    private val coinDetail: CoinDetailDto? = null,
    private val shouldThrow: Boolean = false
) : CryptoApiService {

    override suspend fun getCoins(
        currency: String,
        order: String,
        perPage: Int,
        page: Int
    ): List<CoinDto> {
        if (shouldThrow) throw Exception("API error")
        return coins
    }

    override suspend fun getCoinDetail(coinId: String): CoinDetailDto {
        if (shouldThrow) throw Exception("API error")
        return coinDetail ?: throw Exception("Not found")
    }
}

package com.josegonzalez.jetpackCrypto.data.remote.mapper

import com.josegonzalez.jetpackCrypto.data.remote.model.CoinDetailDto
import com.josegonzalez.jetpackCrypto.data.remote.model.CoinDto
import com.josegonzalez.jetpackCrypto.domain.model.Coin

fun CoinDto.toDomain(): Coin = Coin(
    id = id,
    symbol = symbol.uppercase(),
    name = name,
    imageUrl = image,
    currentPriceUsd = currentPrice ?: 0.0,
    marketCapRank = marketCapRank ?: 0,
    priceChangePercentage24h = priceChangePercentage24h ?: 0.0,
    high24h = high24h ?: 0.0,
    low24h = low24h ?: 0.0,
    lastUpdated = lastUpdated.orEmpty()
)

fun CoinDetailDto.toDomain(): Coin = Coin(
    id = id,
    symbol = symbol.uppercase(),
    name = name,
    imageUrl = image?.large.orEmpty(),
    currentPriceUsd = 0.0,
    marketCapRank = 0,
    priceChangePercentage24h = 0.0,
    high24h = 0.0,
    low24h = 0.0,
    lastUpdated = ""
)

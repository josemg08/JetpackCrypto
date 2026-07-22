package com.josegonzalez.jetpackCrypto.data.local.mapper

import com.josegonzalez.jetpackCrypto.data.local.entity.CoinEntity
import com.josegonzalez.jetpackCrypto.domain.model.Coin

fun CoinEntity.toDomain(): Coin = Coin(
    id = id,
    symbol = symbol,
    name = name,
    imageUrl = imageUrl,
    currentPriceUsd = currentPriceUsd,
    marketCapRank = marketCapRank,
    priceChangePercentage24h = priceChangePercentage24h,
    high24h = high24h,
    low24h = low24h,
    lastUpdated = lastUpdated
)

fun Coin.toEntity(page: Int): CoinEntity = CoinEntity(
    id = id,
    symbol = symbol,
    name = name,
    imageUrl = imageUrl,
    currentPriceUsd = currentPriceUsd,
    marketCapRank = marketCapRank,
    priceChangePercentage24h = priceChangePercentage24h,
    high24h = high24h,
    low24h = low24h,
    lastUpdated = lastUpdated,
    page = page
)
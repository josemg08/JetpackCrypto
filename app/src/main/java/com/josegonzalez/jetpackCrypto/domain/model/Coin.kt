package com.josegonzalez.jetpackCrypto.domain.model

data class Coin(
    val id: String,
    val symbol: String,
    val name: String,
    val imageUrl: String,
    val currentPriceUsd: Double,
    val marketCapRank: Int,
    val priceChangePercentage24h: Double,
    val high24h: Double,
    val low24h: Double,
    val lastUpdated: String
)

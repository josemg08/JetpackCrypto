package com.josegonzalez.jetpackCrypto.data.remote.model

import com.google.gson.annotations.SerializedName

data class CoinDetailDto(
    @SerializedName("id") val id: String,
    @SerializedName("symbol") val symbol: String,
    @SerializedName("name") val name: String,
    @SerializedName("image") val image: ImageDto?,
    @SerializedName("market_data") val marketData: MarketDataDto?
)

data class ImageDto(
    @SerializedName("large") val large: String?
)

data class MarketDataDto(
    @SerializedName("current_price") val currentPrice: Map<String, Double>?,
    @SerializedName("market_cap_rank") val marketCapRank: Int?,
    @SerializedName("price_change_percentage_24h") val priceChangePercentage24h: Double?,
    @SerializedName("high_24h") val high24h: Map<String, Double>?,
    @SerializedName("low_24h") val low24h: Map<String, Double>?,
    @SerializedName("last_updated") val lastUpdated: String?
)

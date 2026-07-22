package com.josegonzalez.jetpackCrypto.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "coins")
data class CoinEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "symbol") val symbol: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "image_url") val imageUrl: String,
    @ColumnInfo(name = "current_price_usd") val currentPriceUsd: Double,
    @ColumnInfo(name = "market_cap_rank") val marketCapRank: Int,
    @ColumnInfo(name = "price_change_24h") val priceChangePercentage24h: Double,
    @ColumnInfo(name = "high_24h") val high24h: Double,
    @ColumnInfo(name = "low_24h") val low24h: Double,
    @ColumnInfo(name = "last_updated") val lastUpdated: String,
    @ColumnInfo(name = "page") val page: Int
)
package com.josegonzalez.jetpackCrypto.data.remote.model

import com.google.gson.annotations.SerializedName

data class CoinDetailDto(
    @SerializedName("id") val id: String,
    @SerializedName("symbol") val symbol: String,
    @SerializedName("name") val name: String,
    @SerializedName("image") val image: ImageDto?
)

data class ImageDto(
    @SerializedName("large") val large: String?
)

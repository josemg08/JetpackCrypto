package com.josegonzalez.jetpackCrypto.domain.model

import android.os.Parcel
import android.os.Parcelable

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
) : Parcelable {

    constructor(parcel: Parcel) : this(
        id = parcel.readString() ?: "",
        symbol = parcel.readString() ?: "",
        name = parcel.readString() ?: "",
        imageUrl = parcel.readString() ?: "",
        currentPriceUsd = parcel.readDouble(),
        marketCapRank = parcel.readInt(),
        priceChangePercentage24h = parcel.readDouble(),
        high24h = parcel.readDouble(),
        low24h = parcel.readDouble(),
        lastUpdated = parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(symbol)
        parcel.writeString(name)
        parcel.writeString(imageUrl)
        parcel.writeDouble(currentPriceUsd)
        parcel.writeInt(marketCapRank)
        parcel.writeDouble(priceChangePercentage24h)
        parcel.writeDouble(high24h)
        parcel.writeDouble(low24h)
        parcel.writeString(lastUpdated)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Coin> {
        override fun createFromParcel(parcel: Parcel): Coin = Coin(parcel)
        override fun newArray(size: Int): Array<Coin?> = arrayOfNulls(size)
    }
}
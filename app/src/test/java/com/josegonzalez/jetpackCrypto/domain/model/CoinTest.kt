package com.josegonzalez.jetpackCrypto.domain.model

import android.os.Parcel
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class CoinTest {

    @Test
    fun `coin parcelable implementation`() {
        val originalCoin = Coin(
            id = "bitcoin",
            symbol = "btc",
            name = "Bitcoin",
            imageUrl = "https://example.com/bitcoin.png",
            currentPriceUsd = 50000.0,
            marketCapRank = 1,
            priceChangePercentage24h = 5.0,
            high24h = 51000.0,
            low24h = 49000.0,
            lastUpdated = "2023-10-27T10:00:00Z"
        )

        val parcel = Parcel.obtain()
        originalCoin.writeToParcel(parcel, originalCoin.describeContents())
        parcel.setDataPosition(0)

        val createdFromParcel = Coin.createFromParcel(parcel)

        assertEquals(originalCoin, createdFromParcel)
        parcel.recycle()
    }

    @Test
    fun `coin describeContents returns zero`() {
        val coin = Coin("bitcoin", "btc", "Bitcoin", "", 50000.0, 1, 5.0, 51000.0, 49000.0, "")
        assertEquals(0, coin.describeContents())
    }

    @Test
    fun `coin newArray returns array of correct size`() {
        val size = 10
        val array = Coin.newArray(size)
        assertEquals(size, array.size)
    }
}

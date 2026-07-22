package com.josegonzalez.jetpackCrypto.data.local.dao

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.josegonzalez.jetpackCrypto.data.local.entity.CoinEntity

@Dao
interface CryptoDao {

    @Query("SELECT * FROM coins ORDER BY market_cap_rank ASC")
    fun getCoinsAsPagingSource(): PagingSource<Int, CoinEntity>

    @Query("SELECT * FROM coins ORDER BY price_change_24h DESC LIMIT 10")
    fun getTopGainers(): LiveData<List<CoinEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(coins: List<CoinEntity>)

    @Query("DELETE FROM coins")
    suspend fun clearAll()
}
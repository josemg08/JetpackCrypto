package com.josegonzalez.jetpackCrypto.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.josegonzalez.jetpackCrypto.data.local.entity.CoinEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CryptoDao {

    @Query("SELECT * FROM coins ORDER BY market_cap_rank ASC")
    fun getCoinsAsPagingSource(): PagingSource<Int, CoinEntity>

    @Query("SELECT * FROM coins ORDER BY price_change_24h DESC LIMIT 10")
    fun getTopGainers(): Flow<List<CoinEntity>>

    @Query("SELECT * FROM coins WHERE name LIKE '%' || :query || '%' OR symbol LIKE '%' || :query || '%' ORDER BY market_cap_rank ASC")
    fun searchCoins(query: String): Flow<List<CoinEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(coins: List<CoinEntity>)

    @Query("DELETE FROM coins")
    suspend fun clearAll()
}
package com.josegonzalez.jetpackCrypto.fake

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.josegonzalez.jetpackCrypto.data.local.dao.CryptoDao
import com.josegonzalez.jetpackCrypto.data.local.entity.CoinEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeCryptoDao : CryptoDao {
    private val coins = mutableListOf<CoinEntity>()
    private val coinsFlow = MutableStateFlow<List<CoinEntity>>(emptyList())
    var wasPagingSourceCreated = false

    override fun getCoinsAsPagingSource(): PagingSource<Int, CoinEntity> {
        wasPagingSourceCreated = true
        return object : PagingSource<Int, CoinEntity>() {
            override fun getRefreshKey(state: PagingState<Int, CoinEntity>): Int? = null
            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CoinEntity> {
                return LoadResult.Page(data = coins, prevKey = null, nextKey = null)
            }
        }
    }

    override fun getTopGainers(): Flow<List<CoinEntity>> {
        return coinsFlow.map { it.take(10) }
    }

    override suspend fun insertAll(coins: List<CoinEntity>) {
        this.coins.addAll(coins)
        coinsFlow.value = this.coins.toList()
    }

    override suspend fun clearAll() {
        this.coins.clear()
        coinsFlow.value = emptyList()
    }
}

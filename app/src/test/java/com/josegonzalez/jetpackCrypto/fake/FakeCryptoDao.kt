package com.josegonzalez.jetpackCrypto.fake

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.josegonzalez.jetpackCrypto.data.local.dao.CryptoDao
import com.josegonzalez.jetpackCrypto.data.local.entity.CoinEntity

class FakeCryptoDao : CryptoDao {
    private val coins = mutableListOf<CoinEntity>()
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

    override fun getTopGainers(): LiveData<List<CoinEntity>> {
        return MutableLiveData(coins.take(10))
    }

    override suspend fun insertAll(coins: List<CoinEntity>) {
        this.coins.addAll(coins)
    }

    override suspend fun clearAll() {
        this.coins.clear()
    }
}

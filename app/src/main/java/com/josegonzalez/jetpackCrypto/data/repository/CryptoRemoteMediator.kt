package com.josegonzalez.jetpackCrypto.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.josegonzalez.jetpackCrypto.data.local.dao.CryptoDao
import com.josegonzalez.jetpackCrypto.data.local.entity.CoinEntity
import com.josegonzalez.jetpackCrypto.data.remote.api.CryptoApiService
import com.josegonzalez.jetpackCrypto.data.remote.mapper.toDomain
import com.josegonzalez.jetpackCrypto.data.local.mapper.toEntity

@OptIn(ExperimentalPagingApi::class)
class CryptoRemoteMediator(
    private val apiService: CryptoApiService,
    private val dao: CryptoDao
) : RemoteMediator<Int, CoinEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CoinEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> 1
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                val lastItem = state.lastItemOrNull()
                    ?: return MediatorResult.Success(endOfPaginationReached = true)
                lastItem.page + 1
            }
        }

        return try {
            val coins = apiService.getCoins(page = page, perPage = state.config.pageSize)
            val entities = coins.map { it.toDomain().toEntity(page) }

            if (loadType == LoadType.REFRESH) dao.clearAll()
            dao.insertAll(entities)

            MediatorResult.Success(endOfPaginationReached = coins.isEmpty())
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}
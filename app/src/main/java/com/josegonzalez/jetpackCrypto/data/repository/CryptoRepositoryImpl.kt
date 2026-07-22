package com.josegonzalez.jetpackCrypto.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.josegonzalez.jetpackCrypto.data.local.dao.CryptoDao
import com.josegonzalez.jetpackCrypto.data.local.mapper.toDomain
import com.josegonzalez.jetpackCrypto.data.remote.api.CryptoApiService
import com.josegonzalez.jetpackCrypto.data.remote.mapper.toDomain
import com.josegonzalez.jetpackCrypto.domain.model.Coin
import com.josegonzalez.jetpackCrypto.domain.repository.CryptoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class CryptoRepositoryImpl(
    private val apiService: CryptoApiService,
    private val dao: CryptoDao
) : CryptoRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getCoinsPaged(): Flow<PagingData<Coin>> =
        Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            remoteMediator = CryptoRemoteMediator(apiService, dao),
            pagingSourceFactory = { dao.getCoinsAsPagingSource() }
        ).flow.map { pagingData -> pagingData.map { it.toDomain() } }

    override suspend fun getCoinDetail(coinId: String): Coin =
        withContext(Dispatchers.IO) {
            apiService.getCoinDetail(coinId).toDomain()
        }
}
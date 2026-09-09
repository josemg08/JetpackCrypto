package com.josegonzalez.jetpackCrypto.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.josegonzalez.jetpackCrypto.data.local.entity.CoinEntity
import com.josegonzalez.jetpackCrypto.data.remote.model.CoinDto
import com.josegonzalez.jetpackCrypto.fake.FakeCryptoApiService
import com.josegonzalez.jetpackCrypto.fake.FakeCryptoDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalPagingApi::class, ExperimentalCoroutinesApi::class)
class CryptoRemoteMediatorTest {

    @Test
    fun `refresh load returns success when more data is present`() = runTest {
        val dto = CoinDto("bitcoin", "BTC", "Bitcoin", "", 50000.0, 1, 0.0, 0.0, 0.0, "")
        val apiService = FakeCryptoApiService(coins = listOf(dto))
        val dao = FakeCryptoDao()
        val mediator = CryptoRemoteMediator(apiService, dao)
        val pagingState = PagingState<Int, CoinEntity>(
            listOf(),
            null,
            PagingConfig(10),
            10
        )

        val result = mediator.load(LoadType.REFRESH, pagingState)

        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @Test
    fun `refresh load returns success and end of pagination when no data`() = runTest {
        val apiService = FakeCryptoApiService(coins = emptyList())
        val dao = FakeCryptoDao()
        val mediator = CryptoRemoteMediator(apiService, dao)
        val pagingState = PagingState<Int, CoinEntity>(
            listOf(),
            null,
            PagingConfig(10),
            10
        )

        val result = mediator.load(LoadType.REFRESH, pagingState)

        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertTrue((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @Test
    fun `refresh load returns error on failure`() = runTest {
        val apiService = FakeCryptoApiService(shouldThrow = true)
        val dao = FakeCryptoDao()
        val mediator = CryptoRemoteMediator(apiService, dao)
        val pagingState = PagingState<Int, CoinEntity>(
            listOf(),
            null,
            PagingConfig(10),
            10
        )

        val result = mediator.load(LoadType.REFRESH, pagingState)

        assertTrue(result is RemoteMediator.MediatorResult.Error)
    }

    @Test
    fun `prepend load returns success with end of pagination`() = runTest {
        val apiService = FakeCryptoApiService()
        val dao = FakeCryptoDao()
        val mediator = CryptoRemoteMediator(apiService, dao)
        val pagingState = PagingState<Int, CoinEntity>(
            listOf(),
            null,
            PagingConfig(10),
            10
        )

        val result = mediator.load(LoadType.PREPEND, pagingState)

        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertTrue((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @Test
    fun `append load returns success when last item is null`() = runTest {
        val apiService = FakeCryptoApiService()
        val dao = FakeCryptoDao()
        val mediator = CryptoRemoteMediator(apiService, dao)
        val pagingState = PagingState<Int, CoinEntity>(
            listOf(),
            null,
            PagingConfig(10),
            10
        )

        val result = mediator.load(LoadType.APPEND, pagingState)

        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertTrue((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @Test
    fun `append load returns success when last item is not null`() = runTest {
        val dto = CoinDto("bitcoin", "BTC", "Bitcoin", "", 50000.0, 1, 0.0, 0.0, 0.0, "")
        val apiService = FakeCryptoApiService(coins = listOf(dto))
        val dao = FakeCryptoDao()
        val mediator = CryptoRemoteMediator(apiService, dao)
        
        val lastItem = CoinEntity("bitcoin", "BTC", "Bitcoin", "", 50000.0, 1, 0.0, 0.0, 0.0, "", 1)
        val pagingState = PagingState<Int, CoinEntity>(
            listOf(PagingSource.LoadResult.Page(listOf(lastItem), null, null)),
            null,
            PagingConfig(10),
            10
        )

        val result = mediator.load(LoadType.APPEND, pagingState)

        assertTrue(result is RemoteMediator.MediatorResult.Success)
    }

    @Test
    fun `append load returns error on failure`() = runTest {
        val apiService = FakeCryptoApiService(shouldThrow = true)
        val dao = FakeCryptoDao()
        val mediator = CryptoRemoteMediator(apiService, dao)
        
        val lastItem = CoinEntity("bitcoin", "BTC", "Bitcoin", "", 50000.0, 1, 0.0, 0.0, 0.0, "", 1)
        val pagingState = PagingState<Int, CoinEntity>(
            listOf(PagingSource.LoadResult.Page(listOf(lastItem), null, null)),
            null,
            PagingConfig(10),
            10
        )

        val result = mediator.load(LoadType.APPEND, pagingState)

        assertTrue(result is RemoteMediator.MediatorResult.Error)
    }
}

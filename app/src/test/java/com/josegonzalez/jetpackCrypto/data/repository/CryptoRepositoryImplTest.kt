package com.josegonzalez.jetpackCrypto.data.repository

import com.josegonzalez.jetpackCrypto.data.remote.model.CoinDetailDto
import com.josegonzalez.jetpackCrypto.data.remote.model.CoinDto
import com.josegonzalez.jetpackCrypto.data.remote.model.ImageDto
import com.josegonzalez.jetpackCrypto.data.remote.model.MarketDataDto
import com.josegonzalez.jetpackCrypto.fake.FakeCryptoApiService
import com.josegonzalez.jetpackCrypto.fake.FakeCryptoDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CryptoRepositoryImplTest {

    @Test
    fun `getCoinsPaged returns flow and triggers paging source factory`() = runTest {
        val apiService = FakeCryptoApiService()
        val dao = FakeCryptoDao()
        val repository = CryptoRepositoryImpl(apiService, dao)

        val flow = repository.getCoinsPaged()
        val result = flow.first()

        assertNotNull(result)
        assertTrue(dao.wasPagingSourceCreated)
    }

    @Test(expected = Exception::class)
    fun `getCoinDetail throws exception when api fails`() = runTest {
        val apiService = FakeCryptoApiService(shouldThrow = true)
        val dao = FakeCryptoDao()
        val repository = CryptoRepositoryImpl(apiService, dao)

        repository.getCoinDetail("bitcoin")
    }
}

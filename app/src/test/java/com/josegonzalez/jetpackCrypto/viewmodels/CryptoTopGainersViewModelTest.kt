package com.josegonzalez.jetpackCrypto.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.josegonzalez.jetpackCrypto.domain.model.Coin
import com.josegonzalez.jetpackCrypto.fake.FakeCryptoNavigation
import com.josegonzalez.jetpackCrypto.fake.FakeCryptoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CryptoTopGainersViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `topGainers exposes data from repository`() = runTest {
        val coin = Coin("bitcoin", "BTC", "Bitcoin", "", 50000.0, 1, 0.0, 0.0, 0.0, "")
        val repository = FakeCryptoRepository(coins = listOf(coin))
        val navigation = FakeCryptoNavigation()
        val viewModel = CryptoTopGainersViewModel(repository, navigation)
        
        val job = launch(UnconfinedTestDispatcher()) { viewModel.topGainers.collect() }
        
        assertEquals(1, viewModel.topGainers.value.size)
        assertEquals(coin, viewModel.topGainers.value[0])
        
        job.cancel()
    }
}

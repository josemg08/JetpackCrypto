package com.josegonzalez.jetpackCrypto.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.josegonzalez.jetpackCrypto.domain.model.Coin
import com.josegonzalez.jetpackCrypto.fake.FakeCryptoRepository
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class CryptoTopGainersViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `topGainers exposes data from repository`() {
        val coin = Coin("bitcoin", "BTC", "Bitcoin", "", 50000.0, 1, 0.0, 0.0, 0.0, "")
        val repository = FakeCryptoRepository(coins = listOf(coin))
        val viewModel = CryptoTopGainersViewModel(repository)

        viewModel.topGainers.observeForever { }
        
        assertEquals(1, viewModel.topGainers.value?.size)
        assertEquals(coin, viewModel.topGainers.value?.get(0))
    }
}

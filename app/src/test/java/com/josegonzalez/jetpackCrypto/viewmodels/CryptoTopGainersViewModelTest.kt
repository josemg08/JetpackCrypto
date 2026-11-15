package com.josegonzalez.jetpackCrypto.viewmodels

import com.josegonzalez.jetpackCrypto.domain.model.Coin
import com.josegonzalez.jetpackCrypto.fake.FakeCryptoNavigation
import com.josegonzalez.jetpackCrypto.fake.FakeCryptoRepository
import com.josegonzalez.jetpackCrypto.ui.contract.CryptoTopGainersUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CryptoTopGainersViewModelTest {

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
    fun `uiState emits Success with coins from repository`() = runTest {
        val coin = Coin("bitcoin", "BTC", "Bitcoin", "", 50000.0, 1, 0.0, 0.0, 0.0, "")
        val repository = FakeCryptoRepository(coins = listOf(coin))
        val navigation = FakeCryptoNavigation()
        val viewModel = CryptoTopGainersViewModel(repository, navigation)

        val state = viewModel.uiState.value
        assertTrue(state is CryptoTopGainersUiState.Success)
        assertTrue((state as CryptoTopGainersUiState.Success).coins.contains(coin))
    }
}

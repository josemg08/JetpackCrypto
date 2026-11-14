package com.josegonzalez.jetpackCrypto.viewmodels

import com.josegonzalez.jetpackCrypto.domain.model.Coin
import com.josegonzalez.jetpackCrypto.fake.FakeCryptoNavigation
import com.josegonzalez.jetpackCrypto.fake.FakeCryptoRepository
import com.josegonzalez.jetpackCrypto.ui.contract.CryptoDetailUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CryptoDetailViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var navigation: FakeCryptoNavigation

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        navigation = FakeCryptoNavigation()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadCoin success sets uiState to Success`() {
        val expectedCoin = Coin("bitcoin", "BTC", "Bitcoin", "", 50000.0, 1, 0.0, 0.0, 0.0, "")
        val repository = FakeCryptoRepository(coinDetail = expectedCoin)
        val viewModel = CryptoDetailViewModel(repository, navigation)

        viewModel.loadCoin("bitcoin")

        val state = viewModel.uiState.value
        assertTrue(state is CryptoDetailUiState.Success)
        assertEquals(expectedCoin, (state as CryptoDetailUiState.Success).coin)
    }

    @Test
    fun `loadCoin error sets uiState to Error`() {
        val repository = FakeCryptoRepository(shouldThrow = true)
        val viewModel = CryptoDetailViewModel(repository, navigation)

        viewModel.loadCoin("bitcoin")

        assertTrue(viewModel.uiState.value is CryptoDetailUiState.Error)
    }

    @Test
    fun `initial uiState is Loading`() {
        val repository = FakeCryptoRepository()
        val viewModel = CryptoDetailViewModel(repository, navigation)

        assertTrue(viewModel.uiState.value is CryptoDetailUiState.Loading)
    }
}

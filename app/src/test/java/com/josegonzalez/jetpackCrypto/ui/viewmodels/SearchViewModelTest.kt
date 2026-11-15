package com.josegonzalez.jetpackCrypto.viewmodels

import com.josegonzalez.jetpackCrypto.domain.model.Coin
import com.josegonzalez.jetpackCrypto.fake.FakeCryptoRepository
import com.josegonzalez.jetpackCrypto.ui.contract.SearchEffect
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
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    private val coins = listOf(
        Coin("1", "BTC", "Bitcoin", "", 0.0, 1, 0.0, 0.0, 0.0, ""),
        Coin("2", "ETH", "Ethereum", "", 0.0, 2, 0.0, 0.0, 0.0, "")
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state has empty query`() = runTest {
        val repository = FakeCryptoRepository(coins = coins)
        val viewModel = SearchViewModel(repository)

        val job = launch(testDispatcher) { viewModel.uiState.collect() }
        assertEquals("", viewModel.uiState.value.query)
        job.cancel()
    }

    @Test
    fun `onQueryChanged updates query in state`() = runTest {
        val repository = FakeCryptoRepository(coins = coins)
        val viewModel = SearchViewModel(repository)

        val job = launch(testDispatcher) { viewModel.uiState.collect() }
        viewModel.onQueryChanged("Bit")
        assertEquals("Bit", viewModel.uiState.value.query)
        job.cancel()
    }
}

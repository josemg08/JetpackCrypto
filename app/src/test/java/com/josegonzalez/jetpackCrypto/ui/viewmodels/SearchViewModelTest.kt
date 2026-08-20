package com.josegonzalez.jetpackCrypto.ui.viewmodels

import com.josegonzalez.jetpackCrypto.domain.model.Coin
import com.josegonzalez.jetpackCrypto.domain.repository.CryptoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    private lateinit var viewModel: SearchViewModel

    @Mock
    private lateinit var repository: CryptoRepository

    private val testDispatcher = StandardTestDispatcher()

    private val mockCoins = listOf(
        Coin("1", "BTC", "Bitcoin", "", 0.0, 1, 0.0, 0.0, 0.0, ""),
        Coin("2", "ETH", "Ethereum", "", 0.0, 2, 0.0, 0.0, 0.0, "")
    )

    private val allCoinsFlow = MutableStateFlow(mockCoins)
    private val filteredCoinsFlow = MutableStateFlow(listOf(mockCoins[0]))

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        
        `when`(repository.searchCoins("")).thenReturn(allCoinsFlow)
        `when`(repository.searchCoins("Bit")).thenReturn(filteredCoinsFlow)
        
        viewModel = SearchViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state contains coins from repository`() = runTest {
        val collectJob = launch(testDispatcher) { viewModel.uiState.collect() }
        advanceUntilIdle()
        
        val state = viewModel.uiState.value
        assertEquals(mockCoins, state.filteredCoins)
        assertEquals("", state.query)
        collectJob.cancel()
    }

    @Test
    fun `filtering calls repository and updates state`() = runTest {
        val collectJob = launch(testDispatcher) { viewModel.uiState.collect() }
        
        viewModel.onQueryChanged("Bit")
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals("Bit", state.query)
        assertEquals(filteredCoinsFlow.value, state.filteredCoins)
        collectJob.cancel()
    }

    @Test
    fun `empty query returns all coins from repository`() = runTest {
        val collectJob = launch(testDispatcher) { viewModel.uiState.collect() }
        
        viewModel.onQueryChanged("Bit")
        advanceUntilIdle()
        
        viewModel.onQueryChanged("")
        advanceUntilIdle()
        
        val state = viewModel.uiState.value
        assertEquals("", state.query)
        assertEquals(mockCoins, state.filteredCoins)
        collectJob.cancel()
    }
}

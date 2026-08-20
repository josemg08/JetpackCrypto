package com.josegonzalez.jetpackCrypto.ui.viewmodels

import com.josegonzalez.jetpackCrypto.domain.model.Coin
import com.josegonzalez.jetpackCrypto.domain.repository.CryptoRepository
import com.josegonzalez.jetpackCrypto.ui.contract.SearchEffect
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
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
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
    fun `initial state contains coins from repository and sheet hidden`() = runTest {
        val collectJob = launch(testDispatcher) { viewModel.uiState.collect() }
        advanceUntilIdle()
        
        val state = viewModel.uiState.value
        assertEquals(mockCoins, state.filteredCoins)
        assertEquals("", state.query)
        assertFalse(state.showSearchSheet)
        collectJob.cancel()
    }

    @Test
    fun `onOpenSearch updates visibility state`() = runTest {
        val collectJob = launch(testDispatcher) { viewModel.uiState.collect() }
        
        viewModel.onOpenSearch()
        advanceUntilIdle()
        
        assertTrue(viewModel.uiState.value.showSearchSheet)
        collectJob.cancel()
    }

    @Test
    fun `onDismissSearch updates visibility state`() = runTest {
        val collectJob = launch(testDispatcher) { viewModel.uiState.collect() }
        
        viewModel.onOpenSearch()
        advanceUntilIdle()
        
        viewModel.onDismissSearch()
        advanceUntilIdle()
        
        assertFalse(viewModel.uiState.value.showSearchSheet)
        collectJob.cancel()
    }

    @Test
    fun `onCoinSelected dismisses sheet and emits navigation effect`() = runTest {
        val collectJob = launch(testDispatcher) { viewModel.uiState.collect() }
        val effects = mutableListOf<SearchEffect>()
        val effectCollectJob = launch(testDispatcher) {
            viewModel.effect.collect { effects.add(it) }
        }
        
        viewModel.onOpenSearch()
        advanceUntilIdle()
        
        val coin = mockCoins[0]
        viewModel.onCoinSelected(coin)
        advanceUntilIdle()
        
        assertFalse(viewModel.uiState.value.showSearchSheet)
        assertEquals(1, effects.size)
        assertTrue(effects[0] is SearchEffect.NavigateToDetail)
        assertEquals(coin, (effects[0] as SearchEffect.NavigateToDetail).coin)
        
        collectJob.cancel()
        effectCollectJob.cancel()
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
}

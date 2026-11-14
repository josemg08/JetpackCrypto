package com.josegonzalez.jetpackCrypto.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingData
import com.josegonzalez.jetpackCrypto.domain.model.Coin
import com.josegonzalez.jetpackCrypto.domain.repository.CryptoRepository
import com.josegonzalez.jetpackCrypto.fake.FakeCryptoNavigation
import com.josegonzalez.jetpackCrypto.fake.FakeCryptoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CryptoListViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

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
    fun `coinsFlow exposes data from repository`() {
        val coins = listOf(
            Coin("bitcoin", "BTC", "Bitcoin", "", 50000.0, 1, 0.0, 0.0, 0.0, ""),
            Coin("ethereum", "ETH", "Ethereum", "", 3000.0, 2, 0.0, 0.0, 0.0, "")
        )
        val repository = FakeCryptoRepository(coins = coins)
        val viewModel = CryptoListViewModel(repository, navigation)

        assertNotNull(viewModel.coinsFlow)
    }

    @Test
    fun `initial isLoading isFalse`() {
        val repository = FakeCryptoRepository(coins = emptyList())
        val viewModel = CryptoListViewModel(repository, navigation)

        assertEquals(false, viewModel.isLoading.value)
    }

    @Test
    fun `initial errorMessageIsNull`() {
        val repository = FakeCryptoRepository(coins = emptyList())
        val viewModel = CryptoListViewModel(repository, navigation)

        assertNull(viewModel.errorMessage.value)
    }

    @Test
    fun `onCoinSelected delegatesToNavigation`() {
        val repository = FakeCryptoRepository(coins = emptyList())
        val viewModel = CryptoListViewModel(repository, navigation)
        val coin = Coin("bitcoin", "BTC", "Bitcoin", "", 50000.0, 1, 0.0, 0.0, 0.0, "")

        viewModel.onCoinSelected(coin)

        assertEquals(1, navigation.navigateToDetailCalls.size)
        assertEquals(coin, navigation.navigateToDetailCalls[0])
    }
}

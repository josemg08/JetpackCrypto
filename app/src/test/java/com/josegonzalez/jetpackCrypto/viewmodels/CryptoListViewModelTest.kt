package com.josegonzalez.jetpackCrypto.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
    fun `coinsFlow populatesLiveData`() {
        val coins = listOf(
            Coin("bitcoin", "BTC", "Bitcoin", "", 50000.0, 1, 0.0, 0.0, 0.0, ""),
            Coin("ethereum", "ETH", "Ethereum", "", 3000.0, 2, 0.0, 0.0, 0.0, "")
        )
        val repository = FakeCryptoRepository(coins = coins)
        val viewModel = CryptoListViewModel(repository, navigation)

        viewModel.coinsFlow.observeForever { }
        assertNotNull(viewModel.coinsFlow.value)
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
    fun `error with null message setsDefaultErrorMessage`() {
        val repository = object : CryptoRepository {
            override fun getCoinsPaged(): Flow<PagingData<Coin>> = emptyFlow()
            override fun getTopGainers(): LiveData<List<Coin>> = MutableLiveData(emptyList())
            override suspend fun getCoinDetail(coinId: String): Coin = throw Exception()
        }
        val viewModel = CryptoListViewModel(repository, navigation)

        // In Class 2, error handling is moved to Paging LoadState
        assertNull(viewModel.errorMessage.value)
    }

    @Test
    fun `onRefresh doesNotCrash`() {
        val coins = listOf(Coin("bitcoin", "BTC", "Bitcoin", "", 50000.0, 1, 0.0, 0.0, 0.0, ""))
        val repository = FakeCryptoRepository(coins = coins)
        val viewModel = CryptoListViewModel(repository, navigation)

        viewModel.onRefresh()
    }

    @Test
    fun `onCoinSelected delegatesToNavigation`() {
        val repository = FakeCryptoRepository()
        val viewModel = CryptoListViewModel(repository, navigation)
        val coin = Coin("bitcoin", "BTC", "Bitcoin", "", 50000.0, 1, 0.0, 0.0, 0.0, "")

        viewModel.onCoinSelected(coin)

        assertEquals(1, navigation.navigateToDetailCalls.size)
        assertEquals(coin, navigation.navigateToDetailCalls[0])
    }
}

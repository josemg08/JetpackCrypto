package com.josegonzalez.jetpackCrypto.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.josegonzalez.jetpackCrypto.domain.model.Coin
import com.josegonzalez.jetpackCrypto.domain.repository.CryptoRepository
import com.josegonzalez.jetpackCrypto.fake.FakeCryptoNavigation
import com.josegonzalez.jetpackCrypto.fake.FakeCryptoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
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
    fun `loadCoins success populatesCoinsLiveData`() {
        val coins = listOf(
            Coin("bitcoin", "BTC", "Bitcoin", "", 50000.0, 1, 0.0, 0.0, 0.0, ""),
            Coin("ethereum", "ETH", "Ethereum", "", 3000.0, 2, 0.0, 0.0, 0.0, "")
        )
        val repository = FakeCryptoRepository(coins = coins)
        val viewModel = CryptoListViewModel(repository, navigation)

        viewModel.loadCoins()

        assertEquals(2, viewModel.coins.value?.size)
        assertEquals("bitcoin", viewModel.coins.value?.get(0)?.id)
    }

    @Test
    fun `loadCoins success setsIsLoadingFalse`() {
        val repository = FakeCryptoRepository(coins = emptyList())
        val viewModel = CryptoListViewModel(repository, navigation)

        viewModel.loadCoins()

        assertEquals(false, viewModel.isLoading.value)
    }

    @Test
    fun `loadCoins success errorMessageIsNull`() {
        val repository = FakeCryptoRepository(coins = emptyList())
        val viewModel = CryptoListViewModel(repository, navigation)

        viewModel.loadCoins()

        assertNull(viewModel.errorMessage.value)
    }

    @Test
    fun `loadCoins error setsErrorMessage`() {
        val repository = FakeCryptoRepository(shouldThrow = true)
        val viewModel = CryptoListViewModel(repository, navigation)

        viewModel.loadCoins()

        assertEquals("Network error", viewModel.errorMessage.value)
    }

    @Test
    fun `loadCoins error with null message setsDefaultErrorMessage`() {
        val repository = object : CryptoRepository {
            override suspend fun getCoins(page: Int, perPage: Int): List<Coin> = throw Exception()
            override suspend fun getCoinDetail(coinId: String): Coin = throw Exception()
        }
        val viewModel = CryptoListViewModel(repository, navigation)

        viewModel.loadCoins()

        assertEquals("An error occurred", viewModel.errorMessage.value)
    }

    @Test
    fun `loadCoins error setsIsLoadingFalse`() {
        val repository = FakeCryptoRepository(shouldThrow = true)
        val viewModel = CryptoListViewModel(repository, navigation)

        viewModel.loadCoins()

        assertEquals(false, viewModel.isLoading.value)
    }

    @Test
    fun `onRefresh reloadsCoins`() {
        val coins = listOf(Coin("bitcoin", "BTC", "Bitcoin", "", 50000.0, 1, 0.0, 0.0, 0.0, ""))
        val repository = FakeCryptoRepository(coins = coins)
        val viewModel = CryptoListViewModel(repository, navigation)

        viewModel.onRefresh()

        assertEquals(1, viewModel.coins.value?.size)
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

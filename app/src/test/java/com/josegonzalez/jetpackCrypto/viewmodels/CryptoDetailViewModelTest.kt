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
class CryptoDetailViewModelTest {

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
    fun `loadCoin success populatesCoinLiveData`() {
        val expectedCoin = Coin("bitcoin", "BTC", "Bitcoin", "", 50000.0, 1, 0.0, 0.0, 0.0, "")
        val repository = FakeCryptoRepository(coinDetail = expectedCoin)
        val viewModel = CryptoDetailViewModel(repository, navigation)

        viewModel.loadCoin("bitcoin")

        assertEquals(expectedCoin, viewModel.coin.value)
    }

    @Test
    fun `loadCoin success setsIsLoadingFalse`() {
        val repository = FakeCryptoRepository()
        val viewModel = CryptoDetailViewModel(repository, navigation)

        viewModel.loadCoin("bitcoin")

        assertEquals(false, viewModel.isLoading.value)
    }

    @Test
    fun `loadCoin success errorMessageIsNull`() {
        val repository = FakeCryptoRepository()
        val viewModel = CryptoDetailViewModel(repository, navigation)

        viewModel.loadCoin("bitcoin")

        assertNull(viewModel.errorMessage.value)
    }

    @Test
    fun `loadCoin error setsErrorMessage`() {
        val repository = FakeCryptoRepository(shouldThrow = true)
        val viewModel = CryptoDetailViewModel(repository, navigation)

        viewModel.loadCoin("bitcoin")

        assertEquals("Network error", viewModel.errorMessage.value)
    }

    @Test
    fun `loadCoin error with null message setsDefaultErrorMessage`() {
        val repository = object : CryptoRepository {
            override suspend fun getCoins(page: Int, perPage: Int): List<Coin> = throw Exception()
            override suspend fun getCoinDetail(coinId: String): Coin = throw Exception()
        }
        val viewModel = CryptoDetailViewModel(repository, navigation)

        viewModel.loadCoin("bitcoin")

        assertEquals("Could not load coin", viewModel.errorMessage.value)
    }

    @Test
    fun `loadCoin error setsIsLoadingFalse`() {
        val repository = FakeCryptoRepository(shouldThrow = true)
        val viewModel = CryptoDetailViewModel(repository, navigation)

        viewModel.loadCoin("bitcoin")

        assertEquals(false, viewModel.isLoading.value)
    }
}

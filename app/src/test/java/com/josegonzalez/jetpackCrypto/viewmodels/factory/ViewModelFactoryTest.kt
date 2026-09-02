package com.josegonzalez.jetpackCrypto.viewmodels.factory

import com.josegonzalez.jetpackCrypto.fake.FakeCryptoNavigation
import com.josegonzalez.jetpackCrypto.fake.FakeCryptoRepository
import com.josegonzalez.jetpackCrypto.viewmodels.CryptoDetailViewModel
import com.josegonzalez.jetpackCrypto.viewmodels.CryptoListViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ViewModelFactoryTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private val repository = FakeCryptoRepository()
    private val navigation = FakeCryptoNavigation()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `CryptoListViewModelFactory creates CryptoListViewModel`() {
        val factory = CryptoListViewModelFactory(repository, navigation)
        val viewModel = factory.create(CryptoListViewModel::class.java)
        assertTrue(viewModel is CryptoListViewModel)
    }

    @Test
    fun `CryptoDetailViewModelFactory creates CryptoDetailViewModel`() {
        val factory = CryptoDetailViewModelFactory(repository, navigation)
        val viewModel = factory.create(CryptoDetailViewModel::class.java)
        assertTrue(viewModel is CryptoDetailViewModel)
    }
}

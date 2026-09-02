package com.josegonzalez.jetpackCrypto.viewmodels.factory

import com.josegonzalez.jetpackCrypto.fake.FakeCryptoNavigation
import com.josegonzalez.jetpackCrypto.fake.FakeCryptoRepository
import com.josegonzalez.jetpackCrypto.viewmodels.CryptoDetailViewModel
import com.josegonzalez.jetpackCrypto.viewmodels.CryptoListViewModel
import org.junit.Assert.assertTrue
import org.junit.Test

class ViewModelFactoryTest {

    private val repository = FakeCryptoRepository()
    private val navigation = FakeCryptoNavigation()

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

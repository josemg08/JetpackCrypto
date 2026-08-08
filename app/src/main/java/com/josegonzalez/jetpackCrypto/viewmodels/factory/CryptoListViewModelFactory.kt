package com.josegonzalez.jetpackCrypto.viewmodels.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.josegonzalez.jetpackCrypto.domain.repository.CryptoRepository
import com.josegonzalez.jetpackCrypto.ui.contract.CryptoNavigation
import com.josegonzalez.jetpackCrypto.viewmodels.CryptoListViewModel

class CryptoListViewModelFactory(
    private val repository: CryptoRepository,
    private val navigation: CryptoNavigation
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return CryptoListViewModel(repository, navigation) as T
    }
}

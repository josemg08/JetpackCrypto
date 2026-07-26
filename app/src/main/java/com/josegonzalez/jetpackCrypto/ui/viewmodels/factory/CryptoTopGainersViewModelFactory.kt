package com.josegonzalez.jetpackCrypto.ui.viewmodels.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.josegonzalez.jetpackCrypto.domain.repository.CryptoRepository
import com.josegonzalez.jetpackCrypto.ui.contract.CryptoNavigation
import com.josegonzalez.jetpackCrypto.ui.viewmodels.CryptoTopGainersViewModel

class CryptoTopGainersViewModelFactory(
    private val repository: CryptoRepository,
    private val navigation: CryptoNavigation
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return CryptoTopGainersViewModel(repository, navigation) as T
    }
}
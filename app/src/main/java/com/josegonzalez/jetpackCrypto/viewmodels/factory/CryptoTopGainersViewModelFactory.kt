package com.josegonzalez.jetpackCrypto.viewmodels.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.josegonzalez.jetpackCrypto.domain.repository.CryptoRepository
import com.josegonzalez.jetpackCrypto.viewmodels.CryptoTopGainersViewModel

class CryptoTopGainersViewModelFactory(
    private val repository: CryptoRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return CryptoTopGainersViewModel(repository) as T
    }
}
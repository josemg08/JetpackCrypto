package com.josegonzalez.jetpackCrypto.viewmodels

import androidx.lifecycle.ViewModel
import com.josegonzalez.jetpackCrypto.domain.repository.CryptoRepository
import com.josegonzalez.jetpackCrypto.ui.contract.CryptoTopGainersContract
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CryptoTopGainersViewModel @Inject constructor(
    repository: CryptoRepository
) : ViewModel(), CryptoTopGainersContract {

    override val topGainers = repository.getTopGainers()
}
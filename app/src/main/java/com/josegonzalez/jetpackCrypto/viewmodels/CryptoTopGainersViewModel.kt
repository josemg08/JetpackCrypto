package com.josegonzalez.jetpackCrypto.viewmodels

import androidx.lifecycle.ViewModel
import com.josegonzalez.jetpackCrypto.domain.repository.CryptoRepository
import com.josegonzalez.jetpackCrypto.ui.contract.CryptoTopGainersContract

class CryptoTopGainersViewModel(
    repository: CryptoRepository
) : ViewModel(), CryptoTopGainersContract {

    override val topGainers = repository.getTopGainers()
}
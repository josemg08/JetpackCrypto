package com.josegonzalez.jetpackCrypto.ui.contract

import com.josegonzalez.jetpackCrypto.domain.model.Coin
import kotlinx.coroutines.flow.StateFlow

interface CryptoTopGainersContract {
    val topGainers: StateFlow<List<Coin>>
    val isLoading: StateFlow<Boolean>
    fun onCoinSelected(coin: Coin)
}

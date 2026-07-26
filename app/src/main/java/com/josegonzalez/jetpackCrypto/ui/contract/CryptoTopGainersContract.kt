package com.josegonzalez.jetpackCrypto.ui.contract

import com.josegonzalez.jetpackCrypto.domain.model.Coin
import kotlinx.coroutines.flow.StateFlow

interface CryptoTopGainersContract {
    val uiState: StateFlow<CryptoTopGainersUiState>
    fun onCoinSelected(coin: Coin)
}
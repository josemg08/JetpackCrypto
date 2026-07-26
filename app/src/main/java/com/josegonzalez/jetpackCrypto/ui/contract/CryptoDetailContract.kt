package com.josegonzalez.jetpackCrypto.ui.contract

import kotlinx.coroutines.flow.StateFlow

interface CryptoDetailContract {
    val uiState: StateFlow<CryptoDetailUiState>
    fun loadCoin(coinId: String)
}
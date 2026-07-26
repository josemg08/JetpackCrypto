package com.josegonzalez.jetpackCrypto.ui.contract

import com.josegonzalez.jetpackCrypto.domain.model.Coin

sealed class CryptoTopGainersUiState {
    object Loading : CryptoTopGainersUiState()
    data class Success(val coins: List<Coin>) : CryptoTopGainersUiState()
    data class Error(val message: String) : CryptoTopGainersUiState()
}
package com.josegonzalez.jetpackCrypto.ui.contract

import com.josegonzalez.jetpackCrypto.domain.model.Coin

sealed class CryptoDetailUiState {
    object Loading : CryptoDetailUiState()
    data class Success(val coin: Coin) : CryptoDetailUiState()
    data class Error(val message: String) : CryptoDetailUiState()
}
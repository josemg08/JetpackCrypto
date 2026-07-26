package com.josegonzalez.jetpackCrypto.ui.contract

sealed class CryptoListUiState {
    object Loading : CryptoListUiState()
    object Success : CryptoListUiState()
    data class Error(val message: String) : CryptoListUiState()
}
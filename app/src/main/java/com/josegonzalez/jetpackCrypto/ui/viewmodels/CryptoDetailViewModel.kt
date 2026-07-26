package com.josegonzalez.jetpackCrypto.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.josegonzalez.jetpackCrypto.domain.repository.CryptoRepository
import com.josegonzalez.jetpackCrypto.ui.contract.CryptoDetailContract
import com.josegonzalez.jetpackCrypto.ui.contract.CryptoDetailUiState
import com.josegonzalez.jetpackCrypto.ui.contract.CryptoNavigation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CryptoDetailViewModel(
    private val repository: CryptoRepository,
    private val navigation: CryptoNavigation
) : ViewModel(), CryptoDetailContract {

    private val _uiState = MutableStateFlow<CryptoDetailUiState>(CryptoDetailUiState.Loading)
    override val uiState: StateFlow<CryptoDetailUiState> = _uiState.asStateFlow()

    override fun loadCoin(coinId: String) {
        viewModelScope.launch {
            _uiState.value = CryptoDetailUiState.Loading
            try {
                _uiState.value = CryptoDetailUiState.Success(repository.getCoinDetail(coinId))
            } catch (e: Exception) {
                _uiState.value = CryptoDetailUiState.Error(e.message ?: "Could not load coin")
            }
        }
    }
}
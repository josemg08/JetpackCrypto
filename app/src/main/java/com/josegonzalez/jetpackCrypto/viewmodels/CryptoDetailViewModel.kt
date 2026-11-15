package com.josegonzalez.jetpackCrypto.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.josegonzalez.jetpackCrypto.domain.repository.CryptoRepository
import com.josegonzalez.jetpackCrypto.ui.contract.CryptoDetailContract
import com.josegonzalez.jetpackCrypto.ui.contract.CryptoDetailUiState
import com.josegonzalez.jetpackCrypto.ui.contract.CryptoNavigation
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CryptoDetailViewModel @AssistedInject constructor(
    private val repository: CryptoRepository,
    @Assisted private val navigation: CryptoNavigation
) : ViewModel(), CryptoDetailContract {

    @AssistedFactory
    interface Factory {
        fun create(navigation: CryptoNavigation): CryptoDetailViewModel
    }

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

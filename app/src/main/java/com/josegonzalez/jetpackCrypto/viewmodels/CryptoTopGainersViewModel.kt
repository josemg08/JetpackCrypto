package com.josegonzalez.jetpackCrypto.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.josegonzalez.jetpackCrypto.domain.model.Coin
import com.josegonzalez.jetpackCrypto.domain.repository.CryptoRepository
import com.josegonzalez.jetpackCrypto.ui.contract.CryptoNavigation
import com.josegonzalez.jetpackCrypto.ui.contract.CryptoTopGainersContract
import com.josegonzalez.jetpackCrypto.ui.contract.CryptoTopGainersUiState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class CryptoTopGainersViewModel @AssistedInject constructor(
    private val repository: CryptoRepository,
    @Assisted private val navigation: CryptoNavigation
) : ViewModel(), CryptoTopGainersContract {

    @AssistedFactory
    interface Factory {
        fun create(navigation: CryptoNavigation): CryptoTopGainersViewModel
    }

    private val _uiState = MutableStateFlow<CryptoTopGainersUiState>(CryptoTopGainersUiState.Loading)
    override val uiState: StateFlow<CryptoTopGainersUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getTopGainers()
                .catch { e -> _uiState.value = CryptoTopGainersUiState.Error(e.message ?: "Error loading top gainers") }
                .collect { coins -> _uiState.value = CryptoTopGainersUiState.Success(coins) }
        }
    }

    override fun onCoinSelected(coin: Coin) = navigation.navigateToDetail(coin)
}

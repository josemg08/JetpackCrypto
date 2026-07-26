package com.josegonzalez.jetpackCrypto.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.josegonzalez.jetpackCrypto.domain.model.Coin
import com.josegonzalez.jetpackCrypto.domain.repository.CryptoRepository
import com.josegonzalez.jetpackCrypto.ui.contract.CryptoListContract
import com.josegonzalez.jetpackCrypto.ui.contract.CryptoListUiState
import com.josegonzalez.jetpackCrypto.ui.contract.CryptoNavigation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class CryptoListViewModel(
    private val repository: CryptoRepository,
    private val navigation: CryptoNavigation
) : ViewModel(), CryptoListContract {

    override val coinsFlow: Flow<PagingData<Coin>> =
        repository.getCoinsPaged().cachedIn(viewModelScope)

    private val _uiState = MutableStateFlow<CryptoListUiState>(CryptoListUiState.Loading)
    override val uiState: StateFlow<CryptoListUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            coinsFlow
                .catch { e -> _uiState.value = CryptoListUiState.Error(e.message ?: "Error loading coins") }
                .collect { _uiState.value = CryptoListUiState.Success }
        }
    }

    override fun onRefresh() {}

    override fun onCoinSelected(coin: Coin) = navigation.navigateToDetail(coin)
}
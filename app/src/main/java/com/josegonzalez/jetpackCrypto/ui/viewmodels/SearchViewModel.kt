package com.josegonzalez.jetpackCrypto.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.josegonzalez.jetpackCrypto.domain.model.Coin
import com.josegonzalez.jetpackCrypto.domain.repository.CryptoRepository
import com.josegonzalez.jetpackCrypto.ui.contract.SearchContract
import com.josegonzalez.jetpackCrypto.ui.contract.SearchUiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModel(
    private val repository: CryptoRepository
) : ViewModel(), SearchContract {

    private val _query = MutableStateFlow("")

    override val uiState: StateFlow<SearchUiState> = _query
        .flatMapLatest { query ->
            repository.searchCoins(query)
        }
        .combine(_query) { coins, query ->
            SearchUiState(query = query, filteredCoins = coins)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SearchUiState()
        )

    override fun onQueryChanged(query: String) {
        _query.value = query
    }

    override fun onCoinSelected(coin: Coin) {
        // Handled by the fragment/navigation
    }
}

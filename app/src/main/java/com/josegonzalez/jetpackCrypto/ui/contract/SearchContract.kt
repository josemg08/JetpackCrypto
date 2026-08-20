package com.josegonzalez.jetpackCrypto.ui.contract

import com.josegonzalez.jetpackCrypto.domain.model.Coin
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

sealed class SearchEffect {
    data class NavigateToDetail(val coin: Coin) : SearchEffect()
}

interface SearchContract {
    val uiState: StateFlow<SearchUiState>
    val effect: SharedFlow<SearchEffect>
    
    fun onQueryChanged(query: String)
    fun onCoinSelected(coin: Coin)
    fun onOpenSearch()
    fun onDismissSearch()
}

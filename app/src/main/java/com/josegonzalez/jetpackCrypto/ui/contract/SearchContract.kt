package com.josegonzalez.jetpackCrypto.ui.contract

import com.josegonzalez.jetpackCrypto.domain.model.Coin
import kotlinx.coroutines.flow.StateFlow

interface SearchContract {
    val uiState: StateFlow<SearchUiState>
    fun onQueryChanged(query: String)
    fun onCoinSelected(coin: Coin)
}

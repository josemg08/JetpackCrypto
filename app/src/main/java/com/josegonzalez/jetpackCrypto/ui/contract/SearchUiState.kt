package com.josegonzalez.jetpackCrypto.ui.contract

import com.josegonzalez.jetpackCrypto.domain.model.Coin

data class SearchUiState(
    val query: String = "",
    val filteredCoins: List<Coin> = emptyList()
)

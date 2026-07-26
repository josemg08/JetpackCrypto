package com.josegonzalez.jetpackCrypto.ui.contract

import androidx.paging.PagingData
import com.josegonzalez.jetpackCrypto.domain.model.Coin
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface CryptoListContract {
    val uiState: StateFlow<CryptoListUiState>
    val coinsFlow: Flow<PagingData<Coin>>

    fun onRefresh()
    fun onCoinSelected(coin: Coin)
}
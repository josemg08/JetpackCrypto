package com.josegonzalez.jetpackCrypto.ui.contract

import androidx.paging.PagingData
import com.josegonzalez.jetpackCrypto.domain.model.Coin
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface CryptoListContract {
    val coinsFlow: Flow<PagingData<Coin>>
    val isLoading: StateFlow<Boolean>
    val errorMessage: StateFlow<String?>

    fun onRefresh()
    fun onCoinSelected(coin: Coin)
}

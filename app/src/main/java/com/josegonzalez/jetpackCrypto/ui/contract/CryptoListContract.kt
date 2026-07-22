package com.josegonzalez.jetpackCrypto.ui.contract

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.josegonzalez.jetpackCrypto.domain.model.Coin

interface CryptoListContract {
    val coinsFlow: LiveData<PagingData<Coin>>
    val isLoading: LiveData<Boolean>
    val errorMessage: LiveData<String?>

    fun onRefresh()
    fun onCoinSelected(coin: Coin)
}
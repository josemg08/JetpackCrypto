package com.josegonzalez.jetpackCrypto.ui.contract

import androidx.lifecycle.LiveData
import com.josegonzalez.jetpackCrypto.domain.model.Coin

interface CryptoListContract {
    val coins: LiveData<List<Coin>>
    val isLoading: LiveData<Boolean>
    val errorMessage: LiveData<String?>

    fun loadCoins()
    fun onRefresh()
    fun onCoinSelected(coin: Coin)
}

package com.josegonzalez.jetpackCrypto.ui.contract

import com.josegonzalez.jetpackCrypto.domain.model.Coin
import kotlinx.coroutines.flow.StateFlow

interface CryptoDetailContract {
    val coin: StateFlow<Coin?>
    val isLoading: StateFlow<Boolean>
    val errorMessage: StateFlow<String?>

    fun loadCoin(coinId: String)
}

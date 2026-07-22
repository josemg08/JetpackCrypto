package com.josegonzalez.jetpackCrypto.ui.contract

import androidx.lifecycle.LiveData
import com.josegonzalez.jetpackCrypto.domain.model.Coin

interface CryptoDetailContract {
    val coin: LiveData<Coin?>
    val isLoading: LiveData<Boolean>
    val errorMessage: LiveData<String?>

    fun loadCoin(coinId: String)
}

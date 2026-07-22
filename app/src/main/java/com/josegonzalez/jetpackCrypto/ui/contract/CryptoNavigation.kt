package com.josegonzalez.jetpackCrypto.ui.contract

import com.josegonzalez.jetpackCrypto.domain.model.Coin

interface CryptoNavigation {
    fun navigateToDetail(coin: Coin)
    fun navigateBack()
}
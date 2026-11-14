package com.josegonzalez.jetpackCrypto.fake

import com.josegonzalez.jetpackCrypto.domain.model.Coin
import com.josegonzalez.jetpackCrypto.ui.contract.CryptoNavigation

class FakeCryptoNavigation : CryptoNavigation {
    var navigateToDetailCalls: MutableList<Coin> = mutableListOf()
    var navigateBackCount = 0

    override fun navigateToDetail(coin: Coin) {
        navigateToDetailCalls.add(coin)
    }

    override fun navigateBack() {
        navigateBackCount++
    }
}

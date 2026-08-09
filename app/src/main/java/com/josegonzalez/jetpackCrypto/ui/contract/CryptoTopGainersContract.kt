package com.josegonzalez.jetpackCrypto.ui.contract

import androidx.lifecycle.LiveData
import com.josegonzalez.jetpackCrypto.domain.model.Coin

interface CryptoTopGainersContract {
    val topGainers: LiveData<List<Coin>>
}
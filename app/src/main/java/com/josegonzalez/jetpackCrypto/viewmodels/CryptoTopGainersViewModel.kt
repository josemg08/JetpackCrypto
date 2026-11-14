package com.josegonzalez.jetpackCrypto.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.josegonzalez.jetpackCrypto.domain.model.Coin
import com.josegonzalez.jetpackCrypto.domain.repository.CryptoRepository
import com.josegonzalez.jetpackCrypto.ui.contract.CryptoNavigation
import com.josegonzalez.jetpackCrypto.ui.contract.CryptoTopGainersContract
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.stateIn

class CryptoTopGainersViewModel @AssistedInject constructor(
    private val repository: CryptoRepository,
    @Assisted private val navigation: CryptoNavigation
) : ViewModel(), CryptoTopGainersContract {

    @AssistedFactory
    interface Factory {
        fun create(navigation: CryptoNavigation): CryptoTopGainersViewModel
    }

    override val topGainers: StateFlow<List<Coin>> = repository.getTopGainers()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    override val isLoading: StateFlow<Boolean> = MutableStateFlow(false)

    override fun onCoinSelected(coin: Coin) {
        navigation.navigateToDetail(coin)
    }
}

package com.josegonzalez.jetpackCrypto.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.josegonzalez.jetpackCrypto.domain.model.Coin
import com.josegonzalez.jetpackCrypto.domain.repository.CryptoRepository
import com.josegonzalez.jetpackCrypto.ui.contract.CryptoListContract
import com.josegonzalez.jetpackCrypto.ui.contract.CryptoNavigation
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CryptoListViewModel @AssistedInject constructor(
    repository: CryptoRepository,
    @Assisted private val navigation: CryptoNavigation
) : ViewModel(), CryptoListContract {

    @AssistedFactory
    interface Factory {
        fun create(navigation: CryptoNavigation): CryptoListViewModel
    }

    override val coinsFlow: Flow<PagingData<Coin>> = repository.getCoinsPaged()
        .cachedIn(viewModelScope)

    override val isLoading: StateFlow<Boolean> = MutableStateFlow(false)
    override val errorMessage: StateFlow<String?> = MutableStateFlow(null)

    override fun onRefresh() {}

    override fun onCoinSelected(coin: Coin) {
        navigation.navigateToDetail(coin)
    }
}

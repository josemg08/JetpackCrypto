package com.josegonzalez.jetpackCrypto.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
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

class CryptoListViewModel @AssistedInject constructor(
    repository: CryptoRepository,
    @Assisted private val navigation: CryptoNavigation
) : ViewModel(), CryptoListContract {

    @AssistedFactory
    interface Factory {
        fun create(navigation: CryptoNavigation): CryptoListViewModel
    }

    override val coinsFlow: LiveData<PagingData<Coin>> = repository.getCoinsPaged()
        .cachedIn(viewModelScope)
        .asLiveData()

    override val isLoading: LiveData<Boolean> = MutableLiveData(false)
    override val errorMessage: LiveData<String?> = MutableLiveData(null)

    override fun onRefresh() {}

    override fun onCoinSelected(coin: Coin) {
        navigation.navigateToDetail(coin)
    }
}

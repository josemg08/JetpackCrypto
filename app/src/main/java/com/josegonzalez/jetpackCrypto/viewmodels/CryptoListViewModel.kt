package com.josegonzalez.jetpackCrypto.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.paging.PagingData
import com.josegonzalez.jetpackCrypto.domain.model.Coin
import com.josegonzalez.jetpackCrypto.domain.repository.CryptoRepository
import com.josegonzalez.jetpackCrypto.ui.contract.CryptoListContract
import com.josegonzalez.jetpackCrypto.ui.contract.CryptoNavigation

class CryptoListViewModel(
    repository: CryptoRepository,
    private val navigation: CryptoNavigation
) : ViewModel(), CryptoListContract {

    override val coinsFlow: LiveData<PagingData<Coin>> = repository.getCoinsPaged().asLiveData()

    override val isLoading: LiveData<Boolean> = MutableLiveData(false)
    override val errorMessage: LiveData<String?> = MutableLiveData(null)

    override fun onRefresh() {}

    override fun onCoinSelected(coin: Coin) = navigation.navigateToDetail(coin)
}
package com.josegonzalez.jetpackCrypto.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.josegonzalez.jetpackCrypto.domain.model.Coin
import com.josegonzalez.jetpackCrypto.domain.repository.CryptoRepository
import com.josegonzalez.jetpackCrypto.ui.contract.CryptoTopGainersContract
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CryptoTopGainersViewModel @Inject constructor(
    repository: CryptoRepository
) : ViewModel(), CryptoTopGainersContract {

    override val topGainers: LiveData<List<Coin>> = repository.getTopGainers().asLiveData()
    override val isLoading: LiveData<Boolean> = MutableLiveData(false)
}
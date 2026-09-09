package com.josegonzalez.jetpackCrypto.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.josegonzalez.jetpackCrypto.domain.model.Coin
import com.josegonzalez.jetpackCrypto.domain.repository.CryptoRepository
import com.josegonzalez.jetpackCrypto.ui.contract.CryptoDetailContract
import com.josegonzalez.jetpackCrypto.ui.contract.CryptoNavigation
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class CryptoDetailViewModel @AssistedInject constructor(
    private val repository: CryptoRepository,
    @Assisted private val navigation: CryptoNavigation
) : ViewModel(), CryptoDetailContract {

    @AssistedFactory
    interface Factory {
        fun create(navigation: CryptoNavigation): CryptoDetailViewModel
    }

    private val _coin = MutableLiveData<Coin?>()
    override val coin: LiveData<Coin?> = _coin

    private val _isLoading = MutableLiveData<Boolean>()
    override val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    override val errorMessage: LiveData<String?> = _errorMessage

    override fun loadCoin(coinId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                _coin.value = repository.getCoinDetail(coinId)
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Could not load coin"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun onBackClicked() {
        navigation.navigateBack()
    }
}

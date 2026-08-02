package com.josegonzalez.jetpackCrypto.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.josegonzalez.jetpackCrypto.domain.model.Coin
import com.josegonzalez.jetpackCrypto.domain.repository.CryptoRepository
import com.josegonzalez.jetpackCrypto.ui.contract.CryptoListContract
import com.josegonzalez.jetpackCrypto.ui.contract.CryptoNavigation
import kotlinx.coroutines.launch

class CryptoListViewModel(
    private val repository: CryptoRepository,
    private val navigation: CryptoNavigation
) : ViewModel(), CryptoListContract {

    private val _coins = MutableLiveData<List<Coin>>()
    override val coins: LiveData<List<Coin>> = _coins

    private val _isLoading = MutableLiveData<Boolean>()
    override val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    override val errorMessage: LiveData<String?> = _errorMessage

    override fun loadCoins() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val result = repository.getCoins(page = 1)
                _coins.value = result
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "An error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }

    override fun onRefresh() = loadCoins()

    override fun onCoinSelected(coin: Coin) = navigation.navigateToDetail(coin)
}

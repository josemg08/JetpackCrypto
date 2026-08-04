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

    private val _coins = MutableLiveData<List<Coin>>() // ViewModel writes here
    override val coins: LiveData<List<Coin>> = _coins  // Activity only sees this

    private val _isLoading = MutableLiveData<Boolean>()     // ViewModel writes here
    override val isLoading: LiveData<Boolean> = _isLoading  // Activity only sees this

    private val _errorMessage = MutableLiveData<String?>()          // ViewModel writes here
    override val errorMessage: LiveData<String?> = _errorMessage    // Activity only sees this

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

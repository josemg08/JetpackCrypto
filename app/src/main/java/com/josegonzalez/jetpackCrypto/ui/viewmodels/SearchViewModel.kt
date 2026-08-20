package com.josegonzalez.jetpackCrypto.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.josegonzalez.jetpackCrypto.domain.model.Coin
import com.josegonzalez.jetpackCrypto.domain.repository.CryptoRepository
import com.josegonzalez.jetpackCrypto.ui.contract.SearchContract
import com.josegonzalez.jetpackCrypto.ui.contract.SearchEffect
import com.josegonzalez.jetpackCrypto.ui.contract.SearchUiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModel(
    private val repository: CryptoRepository
) : ViewModel(), SearchContract {

    private val _query = MutableStateFlow("")
    private val _showSearchSheet = MutableStateFlow(false)
    
    private val _effect = MutableSharedFlow<SearchEffect>()
    override val effect: SharedFlow<SearchEffect> = _effect.asSharedFlow()

    override val uiState: StateFlow<SearchUiState> = combine(
        _query.flatMapLatest { repository.searchCoins(it) },
        _query,
        _showSearchSheet
    ) { coins, query, showSheet ->
        SearchUiState(
            query = query,
            filteredCoins = coins,
            showSearchSheet = showSheet
        )
    }
    .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SearchUiState()
    )

    override fun onQueryChanged(query: String) {
        _query.value = query
    }

    override fun onCoinSelected(coin: Coin) {
        viewModelScope.launch {
            _showSearchSheet.value = false
            _effect.emit(SearchEffect.NavigateToDetail(coin))
        }
    }

    override fun onOpenSearch() {
        _showSearchSheet.value = true
    }

    override fun onDismissSearch() {
        _showSearchSheet.value = false
    }
}

package com.josegonzalez.jetpackCrypto.ui.screens.cryptoListTabs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.josegonzalez.jetpackCrypto.domain.model.Coin
import com.josegonzalez.jetpackCrypto.ui.components.CoinItem
import com.josegonzalez.jetpackCrypto.ui.components.EmptyErrorContent
import com.josegonzalez.jetpackCrypto.ui.contract.CryptoTopGainersContract
import com.josegonzalez.jetpackCrypto.ui.contract.CryptoTopGainersUiState

@Composable
fun TopGainersPage(
    viewModel: CryptoTopGainersContract,
    onCoinClick: (Coin) -> Unit,
    onError: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var lastCoins by remember { mutableStateOf<List<Coin>>(emptyList()) }

    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is CryptoTopGainersUiState.Success -> {
                lastCoins = state.coins
            }
            is CryptoTopGainersUiState.Error -> {
                onError(state.message)
            }
            else -> {}
        }
    }

    val coinsToShow = when (val state = uiState) {
        is CryptoTopGainersUiState.Success -> state.coins
        is CryptoTopGainersUiState.Error -> lastCoins
        is CryptoTopGainersUiState.Loading -> lastCoins
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        if (coinsToShow.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(
                    count = coinsToShow.size,
                    key = { index -> coinsToShow[index].id }
                ) { index ->
                    val coin = coinsToShow[index]
                    val uiModel = remember(coin) { coin }
                    CoinItem(coin = uiModel, onClick = { onCoinClick(coin) })
                    HorizontalDivider()
                }
            }
        } else if (uiState is CryptoTopGainersUiState.Error) {
            EmptyErrorContent(message = (uiState as CryptoTopGainersUiState.Error).message)
        }

        if (uiState is CryptoTopGainersUiState.Loading && coinsToShow.isEmpty()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}

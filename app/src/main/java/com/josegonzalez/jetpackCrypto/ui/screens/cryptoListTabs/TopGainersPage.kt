package com.josegonzalez.jetpackCrypto.ui.screens.cryptoListTabs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.josegonzalez.jetpackCrypto.domain.model.Coin
import com.josegonzalez.jetpackCrypto.ui.contract.CryptoTopGainersContract

@Composable
fun TopGainersPage(
    viewModel: CryptoTopGainersContract,
    onCoinClick: (Coin) -> Unit
) {
    val coins by viewModel.topGainers.observeAsState(emptyList())

    // derivedStateOf to prevent recomposition if the list is empty logic changes.
    val isEmpty by remember {
        derivedStateOf { coins.isEmpty() }
    }

    if (isEmpty) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface),
            contentAlignment = Alignment.Center
        ) {
            Text("Loading top gainers...", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            items(
                count = coins.size,
                key = { index -> coins[index].id }
            ) { index ->
                val coin = coins[index]
                val uiModel = remember(coin) { CoinUiModel(coin) }
                CoinItem(coin = uiModel.coin, onClick = { onCoinClick(coin) })
                HorizontalDivider()
            }
        }
    }
}
package com.josegonzalez.jetpackCrypto.ui.screens.cryptoListTabs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.josegonzalez.jetpackCrypto.domain.model.Coin
import com.josegonzalez.jetpackCrypto.ui.contract.CryptoListContract

@Composable
fun CoinListPage(
    viewModel: CryptoListContract,
    onCoinClick: (Coin) -> Unit
) {
    val lazyPagingItems = viewModel.coinsFlow.collectAsLazyPagingItems()

    // derivedStateOf is crucial here to prevent recomposition when loadState changes
    // unless the actual boolean value (isRefreshing) changes.
    val isRefreshing by remember {
        derivedStateOf { lazyPagingItems.loadState.refresh is LoadState.Loading }
    }

    val error by remember {
        derivedStateOf { lazyPagingItems.loadState.refresh as? LoadState.Error }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(
                count = lazyPagingItems.itemCount,
                // key is essential for LazyColumn performance.
                key = { index -> lazyPagingItems[index]?.id ?: index }
            ) { index ->
                lazyPagingItems[index]?.let { coin ->
                    // Wrapping in a stable UI model (or ensuring Domain model stability).
                    val uiModel = remember(coin) { CoinUiModel(coin) }
                    CoinItem(coin = uiModel.coin, onClick = { onCoinClick(coin) })
                    HorizontalDivider()
                }
            }
            item {
                if (lazyPagingItems.loadState.append is LoadState.Loading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }

        if (isRefreshing && lazyPagingItems.itemCount == 0) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }

        error?.let {
            Text(
                text = it.error.message ?: "Failed to load coins",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(16.dp)
            )
        }
    }
}
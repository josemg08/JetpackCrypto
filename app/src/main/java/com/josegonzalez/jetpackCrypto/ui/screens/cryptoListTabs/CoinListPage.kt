package com.josegonzalez.jetpackCrypto.ui.screens.cryptoListTabs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.josegonzalez.jetpackCrypto.domain.model.Coin
import com.josegonzalez.jetpackCrypto.ui.components.CoinItem
import com.josegonzalez.jetpackCrypto.ui.components.EmptyErrorContent
import com.josegonzalez.jetpackCrypto.ui.contract.CryptoListContract

@Composable
fun CoinListPage(
    viewModel: CryptoListContract,
    onCoinClick: (Coin) -> Unit,
    onError: (String) -> Unit
) {
    val lazyPagingItems = viewModel.coinsFlow.collectAsLazyPagingItems()

    // derivedStateOf is crucial here to prevent recomposition when loadState changes
    // unless the actual boolean value (isRefreshing) changes.
    val isRefreshing by remember {
        derivedStateOf { lazyPagingItems.loadState.refresh is LoadState.Loading }
    }

    val errorState = lazyPagingItems.loadState.refresh as? LoadState.Error

    LaunchedEffect(errorState) {
        if (errorState != null) {
            onError(errorState.error.message ?: "Failed to load coins")
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (lazyPagingItems.itemCount > 0) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(
                    count = lazyPagingItems.itemCount,
                    // key is essential for LazyColumn performance.
                    key = { index -> lazyPagingItems[index]?.id ?: index }
                ) { index ->
                    lazyPagingItems[index]?.let { coin ->
                        val uiModel = remember(coin) { coin }
                        CoinItem(coin = uiModel, onClick = { onCoinClick(coin) })
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
        } else if (errorState != null) {
            EmptyErrorContent(message = errorState.error.message ?: "Failed to load coins")
        }

        if (isRefreshing && lazyPagingItems.itemCount == 0) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}

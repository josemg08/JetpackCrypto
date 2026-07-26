package com.josegonzalez.jetpackCrypto.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.asFlow
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.josegonzalez.jetpackCrypto.domain.model.Coin
import com.josegonzalez.jetpackCrypto.ui.contract.CryptoListContract
import com.josegonzalez.jetpackCrypto.ui.contract.CryptoTopGainersContract
import kotlinx.coroutines.launch

private val tabs = listOf("All Coins", "Top Gainers")

@Composable
fun CryptoListScreen(
    listViewModel: CryptoListContract,
    gainersViewModel: CryptoTopGainersContract
) {
    val pagerState = rememberPagerState { tabs.size }
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = pagerState.currentPage) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
                    text = { Text(title) }
                )
            }
        }
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            when (page) {
                0 -> CoinListPage(viewModel = listViewModel)
                1 -> TopGainersPage(viewModel = gainersViewModel)
            }
        }
    }
}

@Composable
private fun CoinListPage(viewModel: CryptoListContract) {
    val coinsFlow = remember(viewModel) { viewModel.coinsFlow.asFlow() }
    val lazyPagingItems = coinsFlow.collectAsLazyPagingItems()
    val isRefreshing = lazyPagingItems.loadState.refresh is LoadState.Loading
    val error = lazyPagingItems.loadState.refresh as? LoadState.Error

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(count = lazyPagingItems.itemCount) { index ->
                lazyPagingItems[index]?.let { coin ->
                    CoinItem(coin = coin, onClick = { viewModel.onCoinSelected(coin) })
                    HorizontalDivider()
                }
            }
            item {
                if (lazyPagingItems.loadState.append is LoadState.Loading) {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
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
                modifier = Modifier.align(Alignment.Center).padding(16.dp)
            )
        }
    }
}

@Composable
private fun TopGainersPage(viewModel: CryptoTopGainersContract) {
    val coins by viewModel.topGainers.observeAsState(emptyList())

    if (coins.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Loading top gainers...", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    } else {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(count = coins.size) { index ->
                CoinItem(coin = coins[index], onClick = { viewModel.onCoinSelected(coins[index]) })
                HorizontalDivider()
            }
        }
    }
}

@Composable
internal fun CoinItem(coin: Coin, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = coin.imageUrl,
            contentDescription = coin.name,
            modifier = Modifier.size(40.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = coin.name, style = MaterialTheme.typography.bodyLarge)
            Text(
                text = coin.symbol,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "$${String.format("%.2f", coin.currentPriceUsd)}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "${String.format("%.2f", coin.priceChangePercentage24h)}%",
                style = MaterialTheme.typography.bodySmall,
                color = if (coin.priceChangePercentage24h >= 0) Color(0xFF00C853) else Color(0xFFD50000)
            )
        }
    }
}
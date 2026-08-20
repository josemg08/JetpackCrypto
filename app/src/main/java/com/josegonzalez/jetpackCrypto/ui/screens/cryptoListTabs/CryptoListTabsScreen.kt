package com.josegonzalez.jetpackCrypto.ui.screens.cryptoListTabs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.josegonzalez.jetpackCrypto.domain.model.Coin
import com.josegonzalez.jetpackCrypto.ui.contract.CryptoListContract
import com.josegonzalez.jetpackCrypto.ui.contract.CryptoTopGainersContract
import kotlinx.coroutines.launch

/**
 * Using @Immutable to help Compose compiler understand that these models won't change internally.
 * In a real app, this annotation would be on the Domain Model itself or a UI Model wrapper.
 */
@Immutable
data class CoinUiModel(val coin: Coin)

private val tabs = listOf("All Coins", "Top Gainers")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CryptoListTabsScreen(
    listViewModel: CryptoListContract,
    gainersViewModel: CryptoTopGainersContract,
    onCoinClick: (Coin) -> Unit,
    onSearchClick: () -> Unit
) {
    val pagerState = rememberPagerState { tabs.size }
    val scope = rememberCoroutineScope()

    val onTabClick = remember(pagerState, scope) {
        { index: Int ->
            scope.launch { pagerState.animateScrollToPage(index) }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crypto") },
                actions = {
                    IconButton(onClick = onSearchClick) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            TabRow(selectedTabIndex = pagerState.currentPage) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = { onTabClick(index) },
                        text = { Text(title) }
                    )
                }
            }
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                when (page) {
                    0 -> CoinListPage(viewModel = listViewModel, onCoinClick = onCoinClick)
                    1 -> TopGainersPage(viewModel = gainersViewModel, onCoinClick = onCoinClick)
                }
            }
        }
    }
}
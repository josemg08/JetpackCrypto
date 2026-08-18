package com.josegonzalez.jetpackCrypto.ui.screens.cryptoListTabs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.util.Locale
import coil.compose.AsyncImage
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

@Composable
fun CryptoListTabsScreen(
    listViewModel: CryptoListContract,
    gainersViewModel: CryptoTopGainersContract,
    onCoinClick: (Coin) -> Unit
) {
    // rememberPagerState is already an optimization, it remembers the state across recompositions.
    val pagerState = rememberPagerState { tabs.size }
    val scope = rememberCoroutineScope()

    // Using remember for the tab click lambdas to ensure they are stable.
    val onTabClick = remember(pagerState, scope) {
        { index: Int ->
            scope.launch { pagerState.animateScrollToPage(index) }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
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
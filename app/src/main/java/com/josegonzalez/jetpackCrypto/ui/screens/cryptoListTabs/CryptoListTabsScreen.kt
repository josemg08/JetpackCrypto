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
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.josegonzalez.jetpackCrypto.domain.model.Coin
import com.josegonzalez.jetpackCrypto.ui.contract.CryptoListContract
import com.josegonzalez.jetpackCrypto.ui.contract.CryptoTopGainersContract
import com.josegonzalez.jetpackCrypto.ui.contract.SearchContract
import com.josegonzalez.jetpackCrypto.ui.contract.SearchEffect
import kotlinx.coroutines.launch

private val tabs = listOf("All Coins", "Top Gainers")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CryptoListTabsScreen(
    listViewModel: CryptoListContract,
    gainersViewModel: CryptoTopGainersContract,
    searchViewModel: SearchContract,
    onCoinClick: (Coin) -> Unit
) {
    val pagerState = rememberPagerState { tabs.size }
    val scope = rememberCoroutineScope()

    val searchUiState by searchViewModel.uiState.collectAsStateWithLifecycle()
    val sheetState = rememberModalBottomSheetState()

    // Handle One-Time Navigation Effects
    LaunchedEffect(Unit) {
        searchViewModel.effect.collect { effect ->
            when (effect) {
                is SearchEffect.NavigateToDetail -> onCoinClick(effect.coin)
            }
        }
    }

    // Sync BottomSheet sheetState with ViewModel showSearchSheet state
    LaunchedEffect(searchUiState.showSearchSheet) {
        if (searchUiState.showSearchSheet) {
            sheetState.show()
        } else {
            sheetState.hide()
        }
    }

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
                    IconButton(onClick = { searchViewModel.onOpenSearch() }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)) {
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

        if (searchUiState.showSearchSheet) {
            ModalBottomSheet(
                onDismissRequest = { searchViewModel.onDismissSearch() },
                sheetState = sheetState
            ) {
                SearchPage(
                    viewModel = searchViewModel,
                    onCoinClick = { coin -> searchViewModel.onCoinSelected(coin) }
                )
            }
        }
    }
}
package com.josegonzalez.jetpackCrypto.ui.screens.cryptoListTabs

import android.os.Build
import androidx.activity.compose.LocalActivity
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.josegonzalez.jetpackCrypto.domain.model.Coin
import com.josegonzalez.jetpackCrypto.ui.contract.CryptoListContract
import com.josegonzalez.jetpackCrypto.ui.contract.CryptoTopGainersContract
import com.josegonzalez.jetpackCrypto.ui.contract.CryptoTopGainersUiState
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
    onCoinClick: (Coin) -> Unit
) {
    val pagerState = rememberPagerState { tabs.size }
    val scope = rememberCoroutineScope()

    val gainersState by gainersViewModel.uiState.collectAsStateWithLifecycle()

    val activity = LocalActivity.current as FragmentActivity
    val currentOnCoinClick by rememberUpdatedState(onCoinClick)

    // Listen for the coin the user picks inside the fragment and forward it to navigation.
    DisposableEffect(Unit) {
        activity.supportFragmentManager.setFragmentResultListener(
            SearchBottomSheetFragment.RESULT_KEY,
            activity
        ) { _, bundle ->
            val coin: Coin? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle.getParcelable(SearchBottomSheetFragment.ARG_COIN, Coin::class.java)
            } else {
                @Suppress("DEPRECATION")
                bundle.getParcelable(SearchBottomSheetFragment.ARG_COIN)
            }
            coin?.let { currentOnCoinClick(it) }
        }
        onDispose {
            activity.supportFragmentManager.clearFragmentResultListener(
                SearchBottomSheetFragment.RESULT_KEY
            )
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
                    IconButton(onClick = {
                        SearchBottomSheetFragment.newInstance()
                            .show(activity.supportFragmentManager, SearchBottomSheetFragment.TAG)
                    }) {
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
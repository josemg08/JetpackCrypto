package com.josegonzalez.jetpackCrypto.ui.screens.cryptoListTabs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ar.imagin.kouraikhryseai.compose.ui.theme.KTheme
import com.josegonzalez.jetpackCrypto.domain.model.Coin
import com.josegonzalez.jetpackCrypto.ui.contract.SearchContract

@Composable
fun SearchPage(
    viewModel: SearchContract,
    onCoinClick: (Coin) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SearchPageContent(
        value = uiState.query,
        onValueChange = { viewModel.onQueryChanged(it) },
        trailingIcon = {
            if (uiState.query.isNotEmpty()) {
                IconButton(onClick = { viewModel.onQueryChanged("") }) {
                    Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear search")
                }
            }
        },
        coinList = {
            LazyColumn {
                items(uiState.filteredCoins, key = { it.id }) { coin ->
                    CoinItem(coin = coin, onClick = { onCoinClick(coin) })
                }
            }
        }
    )
}

@Composable
fun SearchPageContent(
    value: String,
    onValueChange: (String) -> Unit,
    trailingIcon: @Composable (() -> Unit)? = null,
    coinList: @Composable (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 16.dp)
            .padding(bottom = 32.dp)
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text("Search coins") },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = null)
            },
            trailingIcon = trailingIcon,
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        if (coinList != null) {
            coinList()
        }
    }
}

@Preview
@Composable
private fun SearchPagePreview() {
    val dummyCoins = listOf(
        Coin(
            id = "1",
            name = "Bitcoin",
            symbol = "BTC",
            currentPriceUsd = 40000.0,
            priceChangePercentage24h = 1.0,
            high24h = 41000.0,
            low24h = 39000.0,
            marketCapRank = 1,
            lastUpdated = "2023-09-01T12:00:00Z",
            imageUrl = "https://example.com/btc.png"
        ),
        Coin(
            id = "2",
            name = "Ripple",
            symbol = "XRP",
            currentPriceUsd = 150.0,
            priceChangePercentage24h = -0.5,
            high24h = 2600.0,
            low24h = 2400.0,
            marketCapRank = 2,
            lastUpdated = "2023-09-01T12:00:00Z",
            imageUrl = "https://example.com/eth.png"
        ),
        Coin(
            id = "3",
            name = "Cardano",
            symbol = "ADA",
            currentPriceUsd = 0.5,
            priceChangePercentage24h = 2.3,
            high24h = 0.55,
            low24h = 0.48,
            marketCapRank = 3,
            lastUpdated = "2023-09-01T12:00:00Z",
            imageUrl = "https://example.com/ada.png"
        )
    )

    KTheme(darkTheme = true) {
        SearchPageContent(
            value = "",
            onValueChange = {},
            trailingIcon = null,
            coinList = {
                LazyColumn {
                    items(dummyCoins, key = { it.id }) { coin ->
                        CoinItem(coin = coin, onClick = {})
                    }
                }
            }
        )
    }
}

package com.josegonzalez.jetpackCrypto.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.util.Locale
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.josegonzalez.jetpackCrypto.R
import com.josegonzalez.jetpackCrypto.data.local.database.CryptoDatabase
import com.josegonzalez.jetpackCrypto.data.remote.api.RetrofitClient
import com.josegonzalez.jetpackCrypto.data.repository.CryptoRepositoryImpl
import com.josegonzalez.jetpackCrypto.domain.model.Coin
import com.josegonzalez.jetpackCrypto.ui.contract.CryptoDetailUiState
import com.josegonzalez.jetpackCrypto.ui.contract.CryptoNavigation
import com.josegonzalez.jetpackCrypto.ui.theme.JetpackCryptoTheme
import com.josegonzalez.jetpackCrypto.ui.viewmodels.CryptoDetailViewModel
import com.josegonzalez.jetpackCrypto.ui.viewmodels.factory.CryptoDetailViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CryptoDetailScreen(
    coinId: String,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current

    // Using remember for repository and database to avoid recreation.
    val repository = remember(context) {
        val database = CryptoDatabase.getInstance(context.applicationContext)
        CryptoRepositoryImpl(RetrofitClient.apiService, database.cryptoDao())
    }

    // remember the navigation callback to keep it stable.
    val navigation = remember(onNavigateBack) {
        object : CryptoNavigation {
            override fun navigateToDetail(coin: Coin) {}
            override fun navigateBack() {
                onNavigateBack()
            }
        }
    }

    val factory = remember(repository, navigation) {
        CryptoDetailViewModelFactory(repository, navigation)
    }
    val viewModel: CryptoDetailViewModel = viewModel(factory = factory)

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(coinId) {
        viewModel.loadCoin(coinId)
    }

    val coinName = (uiState as? CryptoDetailUiState.Success)?.coin?.name
        ?: stringResource(R.string.coin_default_detail)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(coinName) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_button_description)
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(padding)
        ) {
            when (uiState) {
                is CryptoDetailUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is CryptoDetailUiState.Error -> {
                    Text(
                        text = (uiState as CryptoDetailUiState.Error).message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp)
                    )
                }
                is CryptoDetailUiState.Success -> {
                    CryptoDetailContent((uiState as CryptoDetailUiState.Success).coin)
                }
            }
        }
    }
}

@Composable
private fun CryptoDetailContent(coin: Coin) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CoinDisplay(
            modifier = Modifier
                .fillMaxWidth(),
            coin = coin
        )
        Spacer(modifier = Modifier.height(24.dp))

        val errorColor = MaterialTheme.colorScheme.error
        val priceText = remember(coin.currentPriceUsd) {
            "$${String.format(Locale.US, "%.2f", coin.currentPriceUsd)}"
        }
        val changeText = remember(coin.priceChangePercentage24h) {
            "${String.format(Locale.US, "%.2f", coin.priceChangePercentage24h)}% (24h)"
        }
        val changeColor = remember(coin.priceChangePercentage24h) {
            if (coin.priceChangePercentage24h >= 0) {
                Color(0xFF00C853)
            } else {
                errorColor
            }
        }

        Text(
            text = priceText,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = changeText,
            style = MaterialTheme.typography.bodyLarge,
            color = changeColor)

        Spacer(modifier = Modifier.height(32.dp))
        HorizontalDivider()

        Spacer(modifier = Modifier.height(16.dp))

        DetailRow(label = "24h High", value = remember(coin.high24h) {
            String.format(Locale.US, "%.2f", coin.high24h)
        })
        DetailRow(label = "24h Low", value = remember(coin.low24h) {
            String.format(Locale.US, "%.2f", coin.low24h)
        })
        DetailRow(label = "Market Cap Rank", value = remember(coin.marketCapRank) { "#${coin.marketCapRank}" })
        DetailRow(label = "Last Updated", value = remember(coin.lastUpdated) { coin.lastUpdated.take(10) })
    }
}

@Composable
private fun CoinDisplay(
    coin: Coin,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.background(MaterialTheme.colorScheme.surface),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = coin.imageUrl,
            contentDescription = coin.name,
            placeholder = painterResource(id = R.drawable.ic_launcher_foreground),
            error = painterResource(id = R.drawable.ic_launcher_foreground),
            modifier = Modifier.size(80.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = coin.name,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = coin.symbol,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

private val defaultCoin = Coin(
    id = "1",
    name = "Bitcoin",
    symbol = "BTC",
    currentPriceUsd = 40000.0,
    priceChangePercentage24h = 0.0,
    high24h = 41000.0,
    low24h = 39000.0,
    marketCapRank = 1,
    lastUpdated = "2023-09-01T12:00:00Z",
    imageUrl = "https://example.com/btc.png"
)

@Preview
@Composable
private fun CryptoDetailContentPreview() {
    JetpackCryptoTheme {
        CryptoDetailContent(
            coin = defaultCoin
        )
    }
}

@Preview
@Composable
private fun CoinDisplayPreview() {
    JetpackCryptoTheme {
        CoinDisplay(
            coin = defaultCoin
        )
    }
}
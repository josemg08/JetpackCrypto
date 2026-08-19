package com.josegonzalez.jetpackCrypto.ui.screens.cryptoListTabs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.josegonzalez.jetpackCrypto.R
import com.josegonzalez.jetpackCrypto.domain.model.Coin
import com.josegonzalez.jetpackCrypto.ui.theme.JetpackCryptoTheme
import java.util.Locale

@Composable
fun CoinItem(
    coin: Coin,
    onClick: () -> Unit
) {
    // remember the click listener to keep it stable.
    val currentOnClick = remember(onClick) { onClick }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .clickable(onClick = currentOnClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = coin.imageUrl,
            placeholder = painterResource(id = R.drawable.ic_launcher_foreground),
            error = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = coin.name,
            modifier = Modifier.size(40.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = coin.name,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = coin.symbol,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        val errorColor = MaterialTheme.colorScheme.error
        // Complex UI logic like string formatting should be remembered.
        val formattedPrice = remember(coin.currentPriceUsd) {
            String.format(Locale.US, "%.2f", coin.currentPriceUsd)
        }
        val formattedPercentage = remember(coin.priceChangePercentage24h) {
            String.format(Locale.US, "%.2f", coin.priceChangePercentage24h)
        }
        val percentageColor = remember(coin.priceChangePercentage24h) {
            if (coin.priceChangePercentage24h >= 0) {
                Color(0xFF00C853)
            } else {
                errorColor
            }
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = formattedPrice,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = formattedPercentage,
                style = MaterialTheme.typography.bodySmall,
                color = percentageColor
            )
        }
    }
}

@Preview
@Composable
fun CoinItemLowPricePreview() {
    JetpackCryptoTheme {
        CoinItem(
            coin = Coin(
                id = "1",
                name = "Bitcoin",
                symbol = "BTC",
                currentPriceUsd = 40000.0,
                priceChangePercentage24h = -1.0,
                high24h = 41000.0,
                low24h = 39000.0,
                marketCapRank = 1,
                lastUpdated = "2023-09-01T12:00:00Z",
                imageUrl = "https://example.com/btc.png"
            ),
            onClick = {}
        )
    }
}

@Preview
@Composable
fun CoinItemHighPricePreview() {
    JetpackCryptoTheme {
        CoinItem(
            coin = Coin(
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
            onClick = {}
        )
    }
}
package com.josegonzalez.jetpackCrypto.ui.components

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.tooling.preview.Preview
import ar.imagin.kouraikhryseai.compose.ui.theme.KTheme
import ar.imagin.kouraikhryseai.compose.ui.theme.KTokens
import ar.imagin.kouraikhryseai.compose.ui.theme.extendedColors
import coil.compose.AsyncImage
import com.josegonzalez.jetpackCrypto.R
import com.josegonzalez.jetpackCrypto.domain.model.Coin
import java.util.Locale

@Composable
fun CoinItem(
    coin: Coin,
    onClick: () -> Unit
) {
    val currentOnClick = remember(onClick) { onClick }

    val errorColor = MaterialTheme.colorScheme.error
    val successColor = KTokens.extendedColors.success
    val formattedPrice = remember(coin.currentPriceUsd) {
        String.format(Locale.US, "%.2f", coin.currentPriceUsd)
    }
    val formattedPercentage = remember(coin.priceChangePercentage24h) {
        String.format(Locale.US, "%.2f", coin.priceChangePercentage24h)
    }
    val percentageColor = remember(coin.priceChangePercentage24h) {
        if (coin.priceChangePercentage24h >= 0) {
            successColor
        } else {
            errorColor
        }
    }
    val priceDirection = remember(coin.priceChangePercentage24h) {
        if (coin.priceChangePercentage24h >= 0) "up" else "down"
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .semantics(mergeDescendants = true) {
                testTagsAsResourceId = true
                testTag = "coin_item_${coin.id}"
                contentDescription =
                    "${coin.name}, ${coin.symbol}, price $formattedPrice, " +
                    "$formattedPercentage percent $priceDirection in the last 24 hours"
                role = Role.Button
            }
            .clickable(onClick = currentOnClick)
            .padding(
                horizontal = KTokens.dimensions.plasticSize.plastic16,
                vertical = KTokens.dimensions.plasticSize.plastic12
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = coin.imageUrl,
            placeholder = painterResource(id = R.drawable.ic_launcher_foreground),
            error = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = null,
            modifier = Modifier
                .size(KTokens.dimensions.plasticSize.plastic37)
                .semantics { testTag = "coin_image_${coin.id}" }
        )
        Spacer(modifier = Modifier.width(KTokens.dimensions.plasticSize.plastic12))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = coin.name,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.semantics { testTag = "coin_name_${coin.id}" }
            )
            Text(
                text = coin.symbol,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.semantics { testTag = "coin_symbol_${coin.id}" }
            )
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = formattedPrice,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.semantics { testTag = "coin_price_${coin.id}" }
            )
            Text(
                text = formattedPercentage,
                style = MaterialTheme.typography.bodySmall,
                color = percentageColor,
                modifier = Modifier.semantics { testTag = "coin_price_change_${coin.id}" }
            )
        }
    }
}

@Preview
@Composable
fun CoinItemLowPricePreview() {
    KTheme(darkTheme = true) {
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
    KTheme(darkTheme = true) {
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
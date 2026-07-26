package com.josegonzalez.jetpackCrypto.ui.activities

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.josegonzalez.jetpackCrypto.domain.model.Coin
import com.josegonzalez.jetpackCrypto.ui.contract.CryptoNavigation
import com.josegonzalez.jetpackCrypto.ui.screens.CryptoDetailScreen
import com.josegonzalez.jetpackCrypto.ui.theme.JetpackCryptoTheme

class CryptoDetailActivity : AppCompatActivity(), CryptoNavigation {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        @Suppress("DEPRECATION")
        val coin = intent.getParcelableExtra<Coin>(EXTRA_COIN) ?: run { finish(); return }

        setContent {
            JetpackCryptoTheme {
                CryptoDetailScreen(
                    coin = coin,
                    onNavigateBack = { navigateBack() }
                )
            }
        }
    }

    override fun navigateToDetail(coin: Coin) = Unit
    override fun navigateBack() = finish()

    companion object {
        const val EXTRA_COIN = "extra_coin"
    }
}
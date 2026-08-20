package com.josegonzalez.jetpackCrypto.ui.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object CryptoList : Screen("crypto_list")
    object CryptoDetail : Screen("crypto_detail/{coinId}") {
        fun createRoute(coinId: String) = "crypto_detail/$coinId"
    }
}
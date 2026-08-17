package com.josegonzalez.jetpackCrypto.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.josegonzalez.jetpackCrypto.ui.screens.CryptoListScreen
import com.josegonzalez.jetpackCrypto.ui.screens.CryptoDetailScreen
import com.josegonzalez.jetpackCrypto.ui.screens.SplashScreen
import com.josegonzalez.jetpackCrypto.ui.contract.CryptoListContract
import com.josegonzalez.jetpackCrypto.ui.contract.CryptoTopGainersContract

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object CryptoList : Screen("crypto_list")
    object CryptoDetail : Screen("crypto_detail/{coinId}") {
        fun createRoute(coinId: String) = "crypto_detail/$coinId"
    }
}

@Composable
fun NavGraph(
    navController: NavHostController,
    listViewModel: CryptoListContract,
    gainersViewModel: CryptoTopGainersContract
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(onSplashFinished = {
                navController.navigate(Screen.CryptoList.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            })
        }
        composable(Screen.CryptoList.route) {
            CryptoListScreen(
                listViewModel = listViewModel,
                gainersViewModel = gainersViewModel,
                onCoinClick = { coin ->
                    navController.navigate(Screen.CryptoDetail.createRoute(coin.id))
                }
            )
        }
        composable(
            route = Screen.CryptoDetail.route,
            arguments = listOf(navArgument("coinId") { type = NavType.StringType })
        ) { backStackEntry ->
            val coinId = backStackEntry.arguments?.getString("coinId") ?: ""
            CryptoDetailScreen(
                coinId = coinId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}

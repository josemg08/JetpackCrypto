package com.josegonzalez.jetpackCrypto.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.josegonzalez.jetpackCrypto.ui.screens.cryptoListTabs.CryptoListTabsScreen
import com.josegonzalez.jetpackCrypto.ui.screens.CryptoDetailScreen
import com.josegonzalez.jetpackCrypto.ui.screens.SplashScreen
import com.josegonzalez.jetpackCrypto.ui.contract.CryptoListContract
import com.josegonzalez.jetpackCrypto.ui.contract.CryptoTopGainersContract
import com.josegonzalez.jetpackCrypto.ui.contract.SearchContract

@Composable
fun NavGraph(
    navController: NavHostController,
    listViewModel: CryptoListContract,
    gainersViewModel: CryptoTopGainersContract,
    searchViewModel: SearchContract
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
            CryptoListTabsScreen(
                listViewModel = listViewModel,
                gainersViewModel = gainersViewModel,
                searchViewModel = searchViewModel,
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
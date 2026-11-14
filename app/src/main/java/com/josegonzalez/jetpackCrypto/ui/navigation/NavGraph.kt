package com.josegonzalez.jetpackCrypto.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.josegonzalez.jetpackCrypto.domain.model.Coin
import com.josegonzalez.jetpackCrypto.ui.screens.cryptoListTabs.CryptoListTabsScreen
import com.josegonzalez.jetpackCrypto.ui.screens.CryptoDetailScreen
import com.josegonzalez.jetpackCrypto.ui.screens.SplashScreen
import com.josegonzalez.jetpackCrypto.ui.contract.CryptoNavigation
import com.josegonzalez.jetpackCrypto.viewmodels.CryptoDetailViewModel
import com.josegonzalez.jetpackCrypto.viewmodels.CryptoListViewModel
import com.josegonzalez.jetpackCrypto.viewmodels.CryptoTopGainersViewModel

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
    listViewModelFactory: CryptoListViewModel.Factory,
    gainersViewModelFactory: CryptoTopGainersViewModel.Factory,
    detailViewModelFactory: CryptoDetailViewModel.Factory
) {
    val navigation = remember(navController) {
        object : CryptoNavigation {
            override fun navigateToDetail(coin: Coin) {
                navController.navigate(Screen.CryptoDetail.createRoute(coin.id))
            }
            override fun navigateBack() {
                navController.popBackStack()
            }
        }
    }

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
            val listViewModel: CryptoListViewModel = viewModel(
                factory = remember {
                    object : ViewModelProvider.Factory {
                        @Suppress("UNCHECKED_CAST")
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            return listViewModelFactory.create(navigation) as T
                        }
                    }
                }
            )
            val gainersViewModel: CryptoTopGainersViewModel = viewModel(
                factory = remember {
                    object : ViewModelProvider.Factory {
                        @Suppress("UNCHECKED_CAST")
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            return gainersViewModelFactory.create(navigation) as T
                        }
                    }
                }
            )
            CryptoListTabsScreen(
                listViewModel = listViewModel,
                gainersViewModel = gainersViewModel,
                onCoinClick = { coin -> navigation.navigateToDetail(coin) }
            )
        }
        composable(
            route = Screen.CryptoDetail.route,
            arguments = listOf(navArgument("coinId") { type = NavType.StringType })
        ) { backStackEntry ->
            val coinId = backStackEntry.arguments?.getString("coinId") ?: ""
            val detailViewModel: CryptoDetailViewModel = viewModel(
                factory = remember {
                    object : ViewModelProvider.Factory {
                        @Suppress("UNCHECKED_CAST")
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            return detailViewModelFactory.create(navigation) as T
                        }
                    }
                }
            )
            CryptoDetailScreen(
                coinId = coinId,
                viewModel = detailViewModel,
                onNavigateBack = { navigation.navigateBack() }
            )
        }
    }
}

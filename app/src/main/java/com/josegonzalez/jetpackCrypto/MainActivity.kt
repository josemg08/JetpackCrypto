package com.josegonzalez.jetpackCrypto

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.compose.rememberNavController
import com.josegonzalez.jetpackCrypto.ui.navigation.NavGraph
import com.josegonzalez.jetpackCrypto.ui.theme.JetpackCryptoTheme
import com.josegonzalez.jetpackCrypto.viewmodels.CryptoDetailViewModel
import com.josegonzalez.jetpackCrypto.viewmodels.CryptoListViewModel
import com.josegonzalez.jetpackCrypto.viewmodels.CryptoTopGainersViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var listViewModelFactory: CryptoListViewModel.Factory

    @Inject
    lateinit var topGainersViewModelFactory: CryptoTopGainersViewModel.Factory

    @Inject
    lateinit var detailViewModelFactory: CryptoDetailViewModel.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            JetpackCryptoTheme {
                val navController = rememberNavController()
                NavGraph(
                    navController = navController,
                    listViewModelFactory = listViewModelFactory,
                    gainersViewModelFactory = topGainersViewModelFactory,
                    detailViewModelFactory = detailViewModelFactory
                )
            }
        }
    }
}

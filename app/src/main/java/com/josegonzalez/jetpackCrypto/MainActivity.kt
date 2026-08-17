package com.josegonzalez.jetpackCrypto

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.josegonzalez.jetpackCrypto.data.local.database.CryptoDatabase
import com.josegonzalez.jetpackCrypto.data.remote.api.RetrofitClient
import com.josegonzalez.jetpackCrypto.data.repository.CryptoRepositoryImpl
import com.josegonzalez.jetpackCrypto.domain.model.Coin
import com.josegonzalez.jetpackCrypto.ui.contract.CryptoNavigation
import com.josegonzalez.jetpackCrypto.ui.navigation.NavGraph
import com.josegonzalez.jetpackCrypto.ui.theme.JetpackCryptoTheme
import com.josegonzalez.jetpackCrypto.ui.viewmodels.CryptoListViewModel
import com.josegonzalez.jetpackCrypto.ui.viewmodels.CryptoTopGainersViewModel
import com.josegonzalez.jetpackCrypto.ui.viewmodels.factory.CryptoListViewModelFactory
import com.josegonzalez.jetpackCrypto.ui.viewmodels.factory.CryptoTopGainersViewModelFactory

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = CryptoDatabase.getInstance(applicationContext)
        val repository = CryptoRepositoryImpl(RetrofitClient.apiService, database.cryptoDao())

        // Dummy navigation for ViewModels - NavGraph handles navigation logic
        val dummyNavigation = object : CryptoNavigation {
            override fun navigateToDetail(coin: Coin) {}
            override fun navigateBack() {}
        }

        val listViewModel = ViewModelProvider(
            this,
            CryptoListViewModelFactory(repository, dummyNavigation)
        )[CryptoListViewModel::class.java]

        val gainersViewModel = ViewModelProvider(
            this,
            CryptoTopGainersViewModelFactory(repository, dummyNavigation)
        )[CryptoTopGainersViewModel::class.java]

        setContent {
            JetpackCryptoTheme {
                val navController = rememberNavController()
                NavGraph(
                    navController = navController,
                    listViewModel = listViewModel,
                    gainersViewModel = gainersViewModel
                )
            }
        }
    }
}

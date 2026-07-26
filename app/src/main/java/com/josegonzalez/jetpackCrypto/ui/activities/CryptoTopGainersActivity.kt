package com.josegonzalez.jetpackCrypto.ui.activities

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.josegonzalez.jetpackCrypto.data.local.database.CryptoDatabase
import com.josegonzalez.jetpackCrypto.data.remote.api.RetrofitClient
import com.josegonzalez.jetpackCrypto.data.repository.CryptoRepositoryImpl
import com.josegonzalez.jetpackCrypto.domain.model.Coin
import com.josegonzalez.jetpackCrypto.ui.contract.CryptoNavigation
import com.josegonzalez.jetpackCrypto.ui.contract.CryptoTopGainersContract
import com.josegonzalez.jetpackCrypto.ui.screens.CryptoTopGainersScreen
import com.josegonzalez.jetpackCrypto.ui.theme.JetpackCryptoTheme
import com.josegonzalez.jetpackCrypto.ui.viewmodels.CryptoTopGainersViewModel
import com.josegonzalez.jetpackCrypto.ui.viewmodels.factory.CryptoTopGainersViewModelFactory

class CryptoTopGainersActivity : AppCompatActivity(), CryptoNavigation {

    private lateinit var viewModel: CryptoTopGainersContract

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = CryptoDatabase.getInstance(applicationContext)
        val repository = CryptoRepositoryImpl(RetrofitClient.apiService, database.cryptoDao())

        viewModel = ViewModelProvider(
            this,
            CryptoTopGainersViewModelFactory(repository, this)
        )[CryptoTopGainersViewModel::class.java]

        setContent {
            JetpackCryptoTheme {
                CryptoTopGainersScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navigateBack() }
                )
            }
        }
    }

    override fun navigateToDetail(coin: Coin) {
        startActivity(
            android.content.Intent(this, CryptoDetailActivity::class.java).apply {
                putExtra(CryptoDetailActivity.EXTRA_COIN, coin)
            }
        )
    }

    override fun navigateBack() = finish()
}
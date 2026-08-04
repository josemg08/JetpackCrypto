package com.josegonzalez.jetpackCrypto.ui.activities

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.josegonzalez.jetpackCrypto.R
import com.josegonzalez.jetpackCrypto.data.local.database.CryptoDatabase
import com.josegonzalez.jetpackCrypto.data.remote.api.RetrofitClient
import com.josegonzalez.jetpackCrypto.data.repository.CryptoRepositoryImpl
import com.josegonzalez.jetpackCrypto.domain.model.Coin
import com.josegonzalez.jetpackCrypto.ui.contract.CryptoDetailContract
import com.josegonzalez.jetpackCrypto.ui.contract.CryptoNavigation
import com.josegonzalez.jetpackCrypto.viewmodels.CryptoDetailViewModel
import com.josegonzalez.jetpackCrypto.viewmodels.factory.CryptoDetailViewModelFactory

class CryptoDetailActivity : AppCompatActivity(), CryptoNavigation {

    private lateinit var viewModel: CryptoDetailContract

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crypto_detail)

        val coinId = intent.getStringExtra(EXTRA_COIN_ID) ?: run { finish(); return }

        val database = CryptoDatabase.getInstance(applicationContext)
        val repository = CryptoRepositoryImpl(RetrofitClient.apiService, database.cryptoDao())
        val factory = CryptoDetailViewModelFactory(repository, this)
        viewModel = ViewModelProvider(this, factory)[CryptoDetailViewModel::class.java]

        val imageView = findViewById<ImageView>(R.id.iv_coin_image)
        val progressBar = findViewById<ProgressBar>(R.id.progress_bar)
        val nameText = findViewById<TextView>(R.id.tv_coin_name)
        val symbolText = findViewById<TextView>(R.id.tv_coin_symbol)
        val priceText = findViewById<TextView>(R.id.tv_coin_price)
        val changeText = findViewById<TextView>(R.id.tv_price_change)
        val highText = findViewById<TextView>(R.id.tv_high_24h)
        val lowText = findViewById<TextView>(R.id.tv_low_24h)
        val lastUpdatedText = findViewById<TextView>(R.id.tv_last_updated)
        val errorText = findViewById<TextView>(R.id.tv_error)

        viewModel.isLoading.observe(this) { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.coin.observe(this) { coin ->
            coin ?: return@observe
            imageView.load(coin.imageUrl) {
                error(R.drawable.ic_launcher_foreground)
                placeholder(R.drawable.ic_launcher_foreground)
            }
            nameText.text = coin.name
            symbolText.text = coin.symbol
            priceText.text = "$%.2f".format(coin.currentPriceUsd)
            changeText.text = "%.2f%%".format(coin.priceChangePercentage24h)
            changeText.setTextColor(
                if (coin.priceChangePercentage24h >= 0)
                    getColor(android.R.color.holo_green_dark)
                else
                    getColor(android.R.color.holo_red_dark)
            )
            highText.text = "$%.2f".format(coin.high24h)
            lowText.text = "$%.2f".format(coin.low24h)
            lastUpdatedText.text = "Updated: ${coin.lastUpdated}"
        }

        viewModel.errorMessage.observe(this) { message ->
            if (message != null) {
                errorText.text = message
                errorText.visibility = View.VISIBLE
            } else {
                errorText.visibility = View.GONE
            }
        }

        viewModel.loadCoin(coinId)
    }

    override fun navigateToDetail(coin: Coin) = Unit

    override fun navigateBack() = finish()

    companion object {
        const val EXTRA_COIN_ID = "coinId"
        const val EXTRA_COIN_NAME = "coinName"
    }
}
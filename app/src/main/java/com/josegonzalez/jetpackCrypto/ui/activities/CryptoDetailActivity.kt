package com.josegonzalez.jetpackCrypto.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import coil.load
import com.josegonzalez.jetpackCrypto.R
import com.josegonzalez.jetpackCrypto.databinding.ActivityCryptoDetailBinding
import com.josegonzalez.jetpackCrypto.domain.model.Coin
import com.josegonzalez.jetpackCrypto.ui.contract.CryptoNavigation
import com.josegonzalez.jetpackCrypto.viewmodels.CryptoDetailViewModel
import com.josegonzalez.jetpackCrypto.viewmodels.factory.CryptoDetailViewModelFactory

class CryptoDetailActivity : AppCompatActivity(), CryptoNavigation {

    private lateinit var binding: ActivityCryptoDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_crypto_detail)

        @Suppress("DEPRECATION")
        val coin = intent.getParcelableExtra<Coin>(EXTRA_COIN) ?: run { finish(); return }

        binding.coin = coin
        binding.ivCoinImage.load(coin.imageUrl) {
            error(R.drawable.ic_launcher_foreground)
            placeholder(R.drawable.ic_launcher_foreground)
        }
    }

    override fun navigateToDetail(coin: Coin) = Unit

    override fun navigateBack() = finish()

    companion object {
        const val EXTRA_COIN = "extra_coin"
    }
}
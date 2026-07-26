package com.josegonzalez.jetpackCrypto.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.josegonzalez.jetpackCrypto.databinding.ActivityCryptoListBinding
import com.josegonzalez.jetpackCrypto.domain.model.Coin
import com.josegonzalez.jetpackCrypto.ui.adapters.CryptoHomePagerAdapter
import com.josegonzalez.jetpackCrypto.ui.contract.CryptoNavigation

class CryptoListActivity : AppCompatActivity(), CryptoNavigation {

    private lateinit var binding: ActivityCryptoListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCryptoListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pagerAdapter = CryptoHomePagerAdapter(this)
        binding.viewPager.adapter = pagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "All Coins"
                1 -> "Top Gainers"
                else -> ""
            }
        }.attach()
    }

    override fun navigateToDetail(coin: Coin) {
        val intent = Intent(this, CryptoDetailActivity::class.java).apply {
            putExtra(CryptoDetailActivity.EXTRA_COIN, coin)
        }
        startActivity(intent)
    }

    override fun navigateBack() = finish()
}
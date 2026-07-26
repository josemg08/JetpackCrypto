package com.josegonzalez.jetpackCrypto.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.josegonzalez.jetpackCrypto.data.local.database.CryptoDatabase
import com.josegonzalez.jetpackCrypto.data.remote.api.RetrofitClient
import com.josegonzalez.jetpackCrypto.data.repository.CryptoRepositoryImpl
import com.josegonzalez.jetpackCrypto.databinding.ActivityCryptoListBinding
import com.josegonzalez.jetpackCrypto.domain.model.Coin
import com.josegonzalez.jetpackCrypto.ui.adapters.CryptoPagingAdapter
import com.josegonzalez.jetpackCrypto.ui.contract.CryptoListContract
import com.josegonzalez.jetpackCrypto.ui.contract.CryptoNavigation
import com.josegonzalez.jetpackCrypto.ui.viewmodels.CryptoListViewModel
import com.josegonzalez.jetpackCrypto.ui.viewmodels.factory.CryptoListViewModelFactory

class CryptoListActivity : AppCompatActivity(), CryptoNavigation {

    private lateinit var binding: ActivityCryptoListBinding
    private lateinit var viewModel: CryptoListContract
    private lateinit var adapter: CryptoPagingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCryptoListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = CryptoDatabase.getInstance(applicationContext)
        val repository = CryptoRepositoryImpl(RetrofitClient.apiService, database.cryptoDao())
        val factory = CryptoListViewModelFactory(repository, this)
        viewModel = ViewModelProvider(this, factory)[CryptoListViewModel::class.java]

        adapter = CryptoPagingAdapter { coin -> viewModel.onCoinSelected(coin) }
        binding.rvCoins.layoutManager = LinearLayoutManager(this)
        binding.rvCoins.adapter = adapter

        adapter.addLoadStateListener { loadState ->
            binding.progressBar.visibility =
                if (loadState.refresh is LoadState.Loading) View.VISIBLE else View.GONE
            binding.swipeRefreshLayout.isRefreshing = false

            val error = loadState.refresh as? LoadState.Error
            binding.tvError.visibility = if (error != null) View.VISIBLE else View.GONE
            binding.tvError.text = error?.error?.message
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            adapter.refresh()
        }

        viewModel.coinsFlow.observe(this) { pagingData ->
            adapter.submitData(lifecycle, pagingData)
        }
    }

    override fun navigateToDetail(coin: Coin) {
        val intent = Intent(this, CryptoDetailActivity::class.java).apply {
            putExtra(CryptoDetailActivity.EXTRA_COIN, coin)
        }
        startActivity(intent)
    }

    override fun navigateBack() = finish()
}
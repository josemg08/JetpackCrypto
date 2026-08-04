package com.josegonzalez.jetpackCrypto.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.josegonzalez.jetpackCrypto.R
import com.josegonzalez.jetpackCrypto.data.local.database.CryptoDatabase
import com.josegonzalez.jetpackCrypto.data.remote.api.RetrofitClient
import com.josegonzalez.jetpackCrypto.data.repository.CryptoRepositoryImpl
import com.josegonzalez.jetpackCrypto.domain.model.Coin
import com.josegonzalez.jetpackCrypto.ui.adapters.CryptoPagingAdapter
import com.josegonzalez.jetpackCrypto.ui.contract.CryptoListContract
import com.josegonzalez.jetpackCrypto.ui.contract.CryptoNavigation
import com.josegonzalez.jetpackCrypto.viewmodels.CryptoListViewModel
import com.josegonzalez.jetpackCrypto.viewmodels.factory.CryptoListViewModelFactory

class CryptoListActivity : AppCompatActivity(), CryptoNavigation {

    private lateinit var viewModel: CryptoListContract
    private lateinit var adapter: CryptoPagingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crypto_list)

        val database = CryptoDatabase.getInstance(applicationContext)
        val repository = CryptoRepositoryImpl(RetrofitClient.apiService, database.cryptoDao())
        val factory = CryptoListViewModelFactory(repository, this)
        viewModel = ViewModelProvider(this, factory)[CryptoListViewModel::class.java]

        val recyclerView = findViewById<RecyclerView>(R.id.rv_coins)
        val progressBar = findViewById<ProgressBar>(R.id.progress_bar)
        val swipeRefresh = findViewById<SwipeRefreshLayout>(R.id.swipe_refresh_layout)
        val errorText = findViewById<TextView>(R.id.tv_error)

        adapter = CryptoPagingAdapter { coin -> viewModel.onCoinSelected(coin) }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        adapter.addLoadStateListener { loadState ->
            progressBar.visibility =
                if (loadState.refresh is LoadState.Loading) View.VISIBLE else View.GONE
            swipeRefresh.isRefreshing = false

            val error = loadState.refresh as? LoadState.Error
            errorText.visibility = if (error != null) View.VISIBLE else View.GONE
            errorText.text = error?.error?.message
        }

        swipeRefresh.setOnRefreshListener {
            adapter.refresh()
        }

        viewModel.coinsFlow.observe(this) { pagingData ->
            adapter.submitData(lifecycle, pagingData)
        }
    }

    override fun navigateToDetail(coin: Coin) {
        val intent = Intent(this, CryptoDetailActivity::class.java).apply {
            putExtra(CryptoDetailActivity.EXTRA_COIN_ID, coin.id)
            putExtra(CryptoDetailActivity.EXTRA_COIN_NAME, coin.name)
        }
        startActivity(intent)
    }

    override fun navigateBack() = finish()
}
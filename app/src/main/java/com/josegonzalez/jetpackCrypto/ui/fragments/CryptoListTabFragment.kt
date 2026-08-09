package com.josegonzalez.jetpackCrypto.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.josegonzalez.jetpackCrypto.data.local.database.CryptoDatabase
import com.josegonzalez.jetpackCrypto.data.remote.api.RetrofitClient
import com.josegonzalez.jetpackCrypto.data.repository.CryptoRepositoryImpl
import com.josegonzalez.jetpackCrypto.databinding.FragmentCryptoListTabBinding
import com.josegonzalez.jetpackCrypto.ui.adapters.CryptoPagingAdapter
import com.josegonzalez.jetpackCrypto.ui.contract.CryptoListContract
import com.josegonzalez.jetpackCrypto.ui.contract.CryptoNavigation
import com.josegonzalez.jetpackCrypto.viewmodels.CryptoListViewModel
import com.josegonzalez.jetpackCrypto.viewmodels.factory.CryptoListViewModelFactory

class CryptoListTabFragment : Fragment() {

    private var _binding: FragmentCryptoListTabBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: CryptoListContract
    private lateinit var adapter: CryptoPagingAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCryptoListTabBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navigation = requireParentFragment() as CryptoNavigation
        val database = CryptoDatabase.getInstance(requireContext())
        val repository = CryptoRepositoryImpl(RetrofitClient.apiService, database.cryptoDao())
        val factory = CryptoListViewModelFactory(repository, navigation)
        viewModel = ViewModelProvider(this, factory)[CryptoListViewModel::class.java]

        adapter = CryptoPagingAdapter { coin -> viewModel.onCoinSelected(coin) }
        binding.rvCoins.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCoins.adapter = adapter

        adapter.addLoadStateListener { loadState ->
            binding.progressBar.visibility =
                if (loadState.refresh is LoadState.Loading) View.VISIBLE else View.GONE
            binding.swipeRefreshLayout.isRefreshing = false

            val error = loadState.refresh as? LoadState.Error
            binding.tvError.visibility = if (error != null) View.VISIBLE else View.GONE
            binding.tvError.text = error?.error?.message
        }

        binding.swipeRefreshLayout.setOnRefreshListener { adapter.refresh() }

        viewModel.coinsFlow.observe(viewLifecycleOwner) { pagingData ->
            adapter.submitData(viewLifecycleOwner.lifecycle, pagingData)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
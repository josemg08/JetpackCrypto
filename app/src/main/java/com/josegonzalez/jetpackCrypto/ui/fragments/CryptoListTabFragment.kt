package com.josegonzalez.jetpackCrypto.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.josegonzalez.jetpackCrypto.databinding.FragmentCryptoListTabBinding
import com.josegonzalez.jetpackCrypto.ui.adapters.CryptoPagingAdapter
import com.josegonzalez.jetpackCrypto.ui.contract.CryptoNavigation
import com.josegonzalez.jetpackCrypto.viewmodels.CryptoListViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CryptoListTabFragment : Fragment() {

    private var _binding: FragmentCryptoListTabBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: CryptoListViewModel.Factory

    private val viewModel: CryptoListViewModel by viewModels {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return viewModelFactory.create(requireParentFragment() as CryptoNavigation) as T
            }
        }
    }

    private lateinit var adapter: CryptoPagingAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCryptoListTabBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
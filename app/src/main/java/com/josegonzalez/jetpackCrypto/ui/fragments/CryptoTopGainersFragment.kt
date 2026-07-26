package com.josegonzalez.jetpackCrypto.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.josegonzalez.jetpackCrypto.data.local.database.CryptoDatabase
import com.josegonzalez.jetpackCrypto.data.remote.api.RetrofitClient
import com.josegonzalez.jetpackCrypto.data.repository.CryptoRepositoryImpl
import com.josegonzalez.jetpackCrypto.databinding.FragmentTopGainersBinding
import com.josegonzalez.jetpackCrypto.ui.adapters.CryptoAdapter
import com.josegonzalez.jetpackCrypto.ui.contract.CryptoNavigation
import com.josegonzalez.jetpackCrypto.ui.contract.CryptoTopGainersContract
import com.josegonzalez.jetpackCrypto.ui.viewmodels.CryptoTopGainersViewModel
import com.josegonzalez.jetpackCrypto.ui.viewmodels.factory.CryptoTopGainersViewModelFactory

class CryptoTopGainersFragment : Fragment() {

    private var _binding: FragmentTopGainersBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: CryptoTopGainersContract
    private lateinit var adapter: CryptoAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTopGainersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navigation = requireActivity() as CryptoNavigation
        val database = CryptoDatabase.getInstance(requireContext())
        val repository = CryptoRepositoryImpl(RetrofitClient.apiService, database.cryptoDao())
        val factory = CryptoTopGainersViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[CryptoTopGainersViewModel::class.java]

        adapter = CryptoAdapter(onCoinClick = { coin -> navigation.navigateToDetail(coin) })
        binding.rvTopGainers.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTopGainers.adapter = adapter

        viewModel.topGainers.observe(viewLifecycleOwner) { coins ->
            adapter.updateCoins(coins)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
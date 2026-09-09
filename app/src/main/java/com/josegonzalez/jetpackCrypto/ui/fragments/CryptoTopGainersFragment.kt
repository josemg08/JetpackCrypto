package com.josegonzalez.jetpackCrypto.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.josegonzalez.jetpackCrypto.databinding.FragmentTopGainersBinding
import com.josegonzalez.jetpackCrypto.ui.adapters.CryptoAdapter
import com.josegonzalez.jetpackCrypto.ui.contract.CryptoNavigation
import com.josegonzalez.jetpackCrypto.viewmodels.CryptoTopGainersViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CryptoTopGainersFragment : Fragment() {

    private var _binding: FragmentTopGainersBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CryptoTopGainersViewModel by viewModels()
    private lateinit var adapter: CryptoAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentTopGainersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navigation = requireParentFragment() as CryptoNavigation

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
package com.josegonzalez.jetpackCrypto.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import coil.load
import com.josegonzalez.jetpackCrypto.R
import com.josegonzalez.jetpackCrypto.databinding.FragmentCryptoDetailBinding
import com.josegonzalez.jetpackCrypto.domain.model.Coin
import com.josegonzalez.jetpackCrypto.ui.contract.CryptoNavigation
import com.josegonzalez.jetpackCrypto.viewmodels.CryptoDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CryptoDetailFragment : Fragment(), CryptoNavigation {

    private var _binding: FragmentCryptoDetailBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: CryptoDetailViewModel.Factory

    private val viewModel: CryptoDetailViewModel by viewModels {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return viewModelFactory.create(this@CryptoDetailFragment) as T
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_crypto_detail, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val coin = CryptoDetailFragmentArgs.fromBundle(requireArguments()).coin
        binding.coin = coin
        binding.ivCoinImage.load(coin.imageUrl) {
            error(R.drawable.ic_launcher_foreground)
            placeholder(R.drawable.ic_launcher_foreground)
        }
        
        viewModel.loadCoin(coin.id)
    }

    override fun navigateToDetail(coin: Coin) {
        // Already in detail
    }

    override fun navigateBack() {
        findNavController().navigateUp()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.josegonzalez.jetpackCrypto.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import coil.load
import com.josegonzalez.jetpackCrypto.R
import com.josegonzalez.jetpackCrypto.databinding.FragmentCryptoDetailBinding

class CryptoDetailFragment : Fragment() {

    private var _binding: FragmentCryptoDetailBinding? = null
    private val binding get() = _binding!!

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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
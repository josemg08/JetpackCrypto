package com.josegonzalez.jetpackCrypto.ui.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.josegonzalez.jetpackCrypto.ui.fragments.CryptoListTabFragment
import com.josegonzalez.jetpackCrypto.ui.fragments.CryptoTopGainersFragment

class CryptoHomePagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> CryptoListTabFragment()
        1 -> CryptoTopGainersFragment()
        else -> throw IllegalStateException("Unexpected tab position: $position")
    }
}
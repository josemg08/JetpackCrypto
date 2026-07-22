package com.josegonzalez.jetpackCrypto.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.josegonzalez.jetpackCrypto.R
import com.josegonzalez.jetpackCrypto.domain.model.Coin

class CryptoPagingAdapter(
    private val onCoinClick: (Coin) -> Unit
) : PagingDataAdapter<Coin, CryptoPagingAdapter.CoinViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Coin>() {
            override fun areItemsTheSame(oldItem: Coin, newItem: Coin) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Coin, newItem: Coin) = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_coin, parent, false)
        return CoinViewHolder(view)
    }

    override fun onBindViewHolder(holder: CoinViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class CoinViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(coin: Coin) {
            itemView.findViewById<ImageView>(R.id.iv_coin_image).load(coin.imageUrl)
            itemView.findViewById<TextView>(R.id.tv_coin_name).text = coin.name
            itemView.findViewById<TextView>(R.id.tv_coin_symbol).text = coin.symbol
            itemView.findViewById<TextView>(R.id.tv_coin_price).text = "$${coin.currentPriceUsd}"
            val changeView = itemView.findViewById<TextView>(R.id.tv_price_change)
            changeView.text = "%.2f%%".format(coin.priceChangePercentage24h)
            changeView.setTextColor(
                if (coin.priceChangePercentage24h >= 0)
                    itemView.context.getColor(android.R.color.holo_green_dark)
                else
                    itemView.context.getColor(android.R.color.holo_red_dark)
            )
            itemView.setOnClickListener { onCoinClick(coin) }
        }
    }
}
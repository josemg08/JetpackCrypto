package com.josegonzalez.jetpackCrypto.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.josegonzalez.jetpackCrypto.R
import com.josegonzalez.jetpackCrypto.domain.model.Coin

class CryptoAdapter(
    private var coins: List<Coin> = emptyList(),
    private val onCoinClick: (Coin) -> Unit
) : RecyclerView.Adapter<CryptoAdapter.CoinViewHolder>() {

    fun updateCoins(newCoins: List<Coin>) {
        coins = newCoins
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_coin, parent, false)
        return CoinViewHolder(view)
    }

    override fun onBindViewHolder(holder: CoinViewHolder, position: Int) {
        holder.bind(coins[position])
    }

    override fun getItemCount() = coins.size

    inner class CoinViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(coin: Coin) {
            itemView.findViewById<ImageView>(R.id.iv_coin_image).load(coin.imageUrl)
            itemView.findViewById<TextView>(R.id.tv_coin_name).text = coin.name
            itemView.findViewById<TextView>(R.id.tv_coin_symbol).text = coin.symbol
            itemView.findViewById<TextView>(R.id.tv_coin_price).text = "$%.2f".format(coin.currentPriceUsd)
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

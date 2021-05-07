package com.example.cryptocurrency.list

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cryptocurrency.AllCurrencies
import com.example.cryptocurrency.Coins
import com.example.cryptocurrency.databinding.FragmentCoinInfoBinding


class CurrencyListAdapter( val currencyListView: AllCurrencies, val onClick : (Coins) -> Unit) : RecyclerView.Adapter<CurrencyListAdapter.ViewHolder>() {

    class ViewHolder(val binding: FragmentCoinInfoBinding, val onClick : (Coins) -> Unit) : RecyclerView.ViewHolder(binding.root){
        fun binding(currency: Coins) {
            val imageString = "https://static.coincap.io/assets/icons/${currency.symbol.toLowerCase()}@2x.png"
            Glide.with(this.itemView).load(imageString).into(binding.currencyImage)
            val correctPriceFormat: String = "$" + currency.priceUsd.substring(0,currency.priceUsd.indexOf(".")+3)
            val correctPercentChangeFormat: String = currency.changePercent24Hr.substring(0,currency.changePercent24Hr.indexOf(".")+3) + "%"
            binding.coinName.text = currency.name
            binding.coinSymbol.text = currency.symbol
            binding.usdBalance.text = correctPriceFormat
            binding.percentage.text = correctPercentChangeFormat
            if(currency.changePercent24Hr[0] == '-'){
                binding.percentage.setTextColor(Color.RED) // finn ut hvordan jeg får det fra drawable
            } else{
                binding.percentage.setTextColor(Color.GREEN)
            }
            binding.root.setOnClickListener{
                onClick(currency)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(FragmentCoinInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false), onClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding(currencyListView.data[position] )
    }

    override fun getItemCount(): Int { // x antall items som skal loade
        return currencyListView.data.size
    }


}
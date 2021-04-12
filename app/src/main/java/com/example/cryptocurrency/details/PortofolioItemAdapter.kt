package com.example.cryptocurrency.details

import android.graphics.Color
import android.text.TextUtils.replace
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cryptocurrency.Coins
import com.example.cryptocurrency.R
import com.example.cryptocurrency.databinding.FragmentCoinInfoBinding
import com.example.cryptocurrency.databinding.FragmentPortofolioBinding
import com.example.cryptocurrency.databinding.FragmentPortofolioItemBinding
import com.example.cryptocurrency.databinding.FragmentTransactionBinding
import com.example.cryptocurrency.entities.Transactions
import com.example.cryptocurrency.list.TransactionsListAdapter

class PortofolioItemAdapter() : RecyclerView.Adapter<PortofolioItemAdapter.ViewHolder>(){

    private val transactionList = mutableListOf<Transactions>()

    class ViewHolder(val binding: FragmentPortofolioItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(transaction: Transactions) {
            // DO SHIT HERE

            val imageString = "https://static.coincap.io/assets/icons/${transaction.coinName.toLowerCase()}@2x.png"
            Glide.with(this.itemView).load(imageString).into(binding.someImgNameHere)
            //val correctPriceFormat: String = "$" + currency.priceUsd.substring(0,currency.priceUsd.indexOf(".")+3)
            //val correctPercentChangeFormat: String = currency.changePercent24Hr.substring(0,currency.changePercent24Hr.indexOf(".")+3) + "%"
            binding.someTextIdHere.text = transaction.coinName
            binding.someTextIdHere2.text = "10903.64 USD"
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Sender med binding

        return ViewHolder(FragmentPortofolioItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
     //holder.binding(currencyListView.data[position] )

        //transactionList[0].coinName
        holder.bind(transactionList[position])

    }

    override fun getItemCount(): Int { // x antall items som skal loade
        return transactionList.size
    }

    fun setTransactionList(list: List<Transactions>) {
        transactionList.clear()
        transactionList.addAll(list)
        notifyDataSetChanged()
    }

}
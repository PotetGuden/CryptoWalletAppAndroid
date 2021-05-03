package com.example.cryptocurrency.details

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cryptocurrency.Coins
import com.example.cryptocurrency.R
import com.example.cryptocurrency.databinding.FragmentPortofolioItemBinding

import com.example.cryptocurrency.entities.Transactions
import com.example.cryptocurrency.list.TransactionsListViewModel
import java.security.acl.Owner


class PortofolioItemAdapter() : RecyclerView.Adapter<PortofolioItemAdapter.ViewHolder>(){

    private val transactionList = mutableListOf<Transactions>()

    private val coinNameList = mutableListOf<String>()
    private val amountOfCoinsList = mutableListOf<Float>()
    private val updatedPriceList = mutableListOf<Coins>()
    //private var usdBalance = Float

    //private val viewModel: TransactionsListViewModel by viewModels()

    class ViewHolder(val binding: FragmentPortofolioItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(coinName: String, amountOfCoin: Float, updatedPrice: String, balanceUsd: Float) {
            if(coinName == "usd"){
                val imageString = "https://static.coincap.io/assets/icons/${coinName}@2x.png"
                Glide.with(this.itemView).load(imageString).into(binding.someImgNameHere)
                binding.someTextIdHere.setTextColor(Color.GREEN)
                binding.someTextIdHere.text = "${balanceUsd} $"
                // Sending balance as USD


            } else{
                val imageString = "https://static.coincap.io/assets/icons/${coinName.toLowerCase()}@2x.png"
                Glide.with(this.itemView).load(imageString).into(binding.someImgNameHere)
                val correctPriceFormat: String = updatedPrice.substring(0,updatedPrice.indexOf(".")+3)
                //val correctPercentChangeFormat: String = currency.changePercent24Hr.substring(0,currency.changePercent24Hr.indexOf(".")+3) + "%"

                binding.someTextIdHere.text = "${amountOfCoin.toString()} x ${correctPriceFormat}"
                val sum = updatedPrice.toFloat()* amountOfCoin
                val correctSum = sum.toString().substring(0,sum.toString().indexOf(".")+3)
                binding.someTextIdHere2.text = "${correctSum} USD"
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Sender med binding

        return ViewHolder(FragmentPortofolioItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
     //holder.binding(currencyListView.data[position] )
        var balanceUsd = 0F
        for (i in transactionList.indices) {
            balanceUsd += transactionList[i].updatedPrice*transactionList[i].amountOfCoin
        }
        Log.d("Coin Name List Size: ", coinNameList.size.toString())
        //transactionList[0].coinName
        for(i in 0..updatedPriceList.size-1){
            if(updatedPriceList[i].symbol == coinNameList[position]){
                Log.d("UpdatedPriceList Symbol / coinNameList: ", updatedPriceList[i].symbol + " " + coinNameList[position])
                holder.bind(coinNameList[position], amountOfCoinsList[position], updatedPriceList[i].priceUsd, balanceUsd)
            } else if(coinNameList[position] == "usd"){
                holder.bind("usd", 1F, "10000", balanceUsd * -1F)
            }
        }
        //holder.bind(transactionList[position], coinNameList[position], amountOfCoinsList[position])
    }

    override fun getItemCount(): Int { // x antall items som skal loade
        return coinNameList.size
    }

    fun setTransactionList(list: List<Transactions>) {
        transactionList.clear()
        transactionList.addAll(list)
        notifyDataSetChanged()
    }

    fun setAmountOfCoinsList(list: List<Float>) {
        amountOfCoinsList.clear()
        amountOfCoinsList.addAll(list)
        notifyDataSetChanged()
    }

    fun setCoinNameList(list: List<String>) {
        coinNameList.clear()
        coinNameList.addAll(list)
        notifyDataSetChanged()
    }

    fun setUpdatedPriceList(list: List<Coins>){
        updatedPriceList.clear()
        updatedPriceList.addAll(list)
        notifyDataSetChanged()
    }

    fun setUsdBalance(_usdBalance: Float){
        //usdBalance = _usdBalance
    }



}
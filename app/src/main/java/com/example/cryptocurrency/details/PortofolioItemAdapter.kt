package com.example.cryptocurrency.details

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cryptocurrency.Coins
import com.example.cryptocurrency.databinding.FragmentPortofolioItemBinding

import com.example.cryptocurrency.entities.Transactions


class PortofolioItemAdapter() : RecyclerView.Adapter<PortofolioItemAdapter.ViewHolder>(){

    //private val transactionList = mutableListOf<Transactions>()

    private val coinNameList = mutableListOf<String>()
    private val amountOfCoinsList = mutableListOf<Float>()
    private val updatedPriceList = mutableListOf<Coins>()


    class ViewHolder(val binding: FragmentPortofolioItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(coinName: String, amountOfCoin: Float, updatedPrice: String) {
            // DO SHIT HERE

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Sender med binding

        return ViewHolder(FragmentPortofolioItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
     //holder.binding(currencyListView.data[position] )
        Log.d("Coin Name List Size: ", coinNameList.size.toString())
        //transactionList[0].coinName
        for(i in 0..updatedPriceList.size-1){
            if(updatedPriceList[i].symbol == coinNameList[position]){
                Log.d("UpdatedPriceList Symbol / coinNameList: ", updatedPriceList[i].symbol + " " + coinNameList[position])
                holder.bind(coinNameList[position], amountOfCoinsList[position], updatedPriceList[i].priceUsd)
            }
        }
        //holder.bind(transactionList[position], coinNameList[position], amountOfCoinsList[position])
    }

    override fun getItemCount(): Int { // x antall items som skal loade
        return coinNameList.size
    }

    /*fun setTransactionList(list: List<Transactions>) {
        transactionList.clear()
        transactionList.addAll(list)
        notifyDataSetChanged()
    }*/

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



}
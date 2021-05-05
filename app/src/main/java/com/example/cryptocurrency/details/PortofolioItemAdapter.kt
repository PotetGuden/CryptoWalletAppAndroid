package com.example.cryptocurrency.details

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cryptocurrency.Coins
import com.example.cryptocurrency.databinding.FragmentPortofolioItemBinding
import com.example.cryptocurrency.entities.Transactions
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import kotlin.math.absoluteValue


class PortofolioItemAdapter() : RecyclerView.Adapter<PortofolioItemAdapter.ViewHolder>(){

    private val transactionList = mutableListOf<Transactions>()
    private val coinNameList = mutableListOf<String>()
    private val amountOfCoinsList = mutableListOf<Float>()
    private val updatedPriceList = mutableListOf<Coins>()

    class ViewHolder(val binding: FragmentPortofolioItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(coinSymbol: String, amountOfCoin: Float, updatedPrice: String, balanceUsd: Float, coinName: String) {
            if(coinSymbol == "usd"){
                val imageString = "https://static.coincap.io/assets/icons/${coinSymbol}@2x.png"
                Glide.with(this.itemView).load(imageString).into(binding.someImgNameHere)
                val balanceUsdFormatted = BigDecimal(balanceUsd.toDouble()).setScale(2, RoundingMode.HALF_EVEN)
                binding.coinName.text = "Dollar"
                binding.coinSymbol.text = coinName
                binding.amountOfCoinsAndSymbol.text = "$$balanceUsdFormatted"
                binding.usdBalance.text = ""

            } else{
                val usdBalanced = BigDecimal(amountOfCoin.toDouble() * updatedPrice.toDouble() ).setScale(2, RoundingMode.HALF_EVEN)
                val amountOfCoinsFormatted = if( amountOfCoin.absoluteValue % 1.0 <= 0.01){
                    BigDecimal(amountOfCoin.toDouble()).setScale(4, RoundingMode.HALF_EVEN)
                } else{
                    BigDecimal(amountOfCoin.toDouble()).setScale(2, RoundingMode.HALF_EVEN)
                }

                val imageString = "https://static.coincap.io/assets/icons/${coinSymbol.toLowerCase()}@2x.png"

                Glide.with(this.itemView).load(imageString).into(binding.someImgNameHere)
                binding.coinName.text = coinName
                binding.coinSymbol.text = coinSymbol
                binding.amountOfCoinsAndSymbol.text = "$amountOfCoinsFormatted ${coinSymbol.toUpperCase()}"
                binding.usdBalance.text = "$${usdBalanced}"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(FragmentPortofolioItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var balanceUsd = 0F
        for (transaction in transactionList) {
            balanceUsd += transaction.updatedPrice * transaction.amountOfCoin
        }

        for(currency in updatedPriceList){
            if(currency.symbol == coinNameList[position]){
                holder.bind(coinNameList[position], amountOfCoinsList[position], currency.priceUsd, balanceUsd, currency.name)
            } else if(coinNameList[position] == "usd"){
                holder.bind("usd", 1F, "10000", balanceUsd * -1F, "USD")
            }
        }

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
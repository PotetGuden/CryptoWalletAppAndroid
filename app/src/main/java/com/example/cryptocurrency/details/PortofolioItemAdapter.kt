package com.example.cryptocurrency.details

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cryptocurrency.Coins
import com.example.cryptocurrency.databinding.FragmentPortofolioItemBinding
import com.example.cryptocurrency.entities.GroupedSumAndNameTransaction
import com.example.cryptocurrency.entities.Transactions
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.absoluteValue


class PortofolioItemAdapter() : RecyclerView.Adapter<PortofolioItemAdapter.ViewHolder>(){

    private val transactionList = mutableListOf<GroupedSumAndNameTransaction>()
    private val updatedPriceList = mutableListOf<Coins>()

    class ViewHolder(val binding: FragmentPortofolioItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(transactionGrouped: GroupedSumAndNameTransaction, updatedCryptoApi : Coins, usdBalance : Float) {
            if(transactionGrouped.coinName == "usd"){
                val imageString = "https://static.coincap.io/assets/icons/${transactionGrouped.coinName}@2x.png"
                Glide.with(this.itemView).load(imageString).into(binding.someImgNameHere)
                val balanceUsdFormatted = BigDecimal(usdBalance.toDouble()).setScale(2, RoundingMode.HALF_EVEN)
                binding.coinName.text = "Dollar"
                binding.coinSymbol.text = "USD"
                //binding.amountOfCoinsAndSymbol.text = "$$balanceUsdFormatted"
                binding.amountOfCoinsAndSymbol.text = "$$balanceUsdFormatted"

                binding.usdBalance.text = ""

            } else{
                val usdBalanced = BigDecimal(transactionGrouped.sumAmountCoins.toDouble() * updatedCryptoApi.priceUsd.toDouble() ).setScale(2, RoundingMode.HALF_EVEN)
                val amountOfCoinsFormatted = if( transactionGrouped.sumAmountCoins.absoluteValue % 1.0 <= 0.01){
                    BigDecimal(transactionGrouped.sumAmountCoins.toDouble()).setScale(4, RoundingMode.HALF_EVEN)
                } else{
                    BigDecimal(transactionGrouped.sumAmountCoins.toDouble()).setScale(2, RoundingMode.HALF_EVEN)
                }

                val imageString = "https://static.coincap.io/assets/icons/${transactionGrouped.coinName.toLowerCase()}@2x.png"

                Glide.with(this.itemView).load(imageString).into(binding.someImgNameHere)
                binding.coinName.text = updatedCryptoApi.name
                binding.coinSymbol.text = transactionGrouped.coinName
                binding.amountOfCoinsAndSymbol.text = "$amountOfCoinsFormatted ${transactionGrouped.coinName.toUpperCase()}"
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
            balanceUsd += transaction.balanceUsd
        }

        for(currency in updatedPriceList){
            if(currency.symbol == transactionList[position].coinName){
                holder.bind(transactionList[position], currency, balanceUsd)
            } else if (transactionList[position].coinName == "usd"){
                holder.bind(transactionList[position], currency, balanceUsd*-1F)
            }
        }
        Log.d("PortofolioAdapter", "onBindViewHolder")
    }

    override fun getItemCount(): Int {
        return transactionList.size
    }

    fun setTransactionList(list: List<GroupedSumAndNameTransaction>) {
        Log.d("PortofolioAdapter", "setTransactionList")
        transactionList.clear()
        transactionList.addAll(list)
        notifyDataSetChanged()
        //Log.d("PortofolioAdapter", "currencyName: ${transactionList[0].coinName} updated balance: ${transactionList[0].amountOfCoin * transactionList[0].updatedPrice}")
    }

    fun setUpdatedPriceList(list: List<Coins>){
        Log.d("PortofolioAdapter", "setUpdatedPriceList")
        updatedPriceList.clear()
        updatedPriceList.addAll(list)
        notifyDataSetChanged()
    }

}
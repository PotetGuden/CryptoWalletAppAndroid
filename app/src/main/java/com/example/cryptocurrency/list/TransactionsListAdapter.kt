package com.example.cryptocurrency.list

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cryptocurrency.databinding.FragmentTransactionBinding
import com.example.cryptocurrency.entities.Transactions
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

class TransactionsListAdapter() : RecyclerView.Adapter<TransactionsListAdapter.ViewHolder>(){

    private val transactionList = mutableListOf<Transactions>()

    class ViewHolder(val binding: FragmentTransactionBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(transaction: Transactions) {
            Glide.with(this.itemView).load("https://static.coincap.io/assets/icons/${transaction.coinName.toLowerCase()}@2x.png").into(
                binding.someImgNameHere
            )
            var amountOfCoins : Float = transaction.amountOfCoin
            val coinName : String = transaction.coinName
            val updatedPrice : Float = transaction.updatedPrice
            val dateTime : String = transaction.transactionDate

            if(coinName == "usd"){
                binding.coinName.text = "Installation Reward ${getEmoji(0x1F4B0)} ${getEmoji(0x1F911)}"
                binding.coinName.setTextColor(Color.RED)
                binding.someTextIdHere2.text = "10000 $"
                binding.someTextIdHere2.setTextColor(Color.GREEN)
                binding.someTextIdHere3.text = dateTime
            } else{
                val df = DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH))
                df.maximumFractionDigits = 2
                var usdValue : String = ""

                if (amountOfCoins < 0){
                    amountOfCoins *= -1F  // So the printed values will be positive
                    binding.coinName.text = "SOLD"
                    binding.coinName.setTextColor(Color.RED)
                    usdValue = df.format(updatedPrice * amountOfCoins)
                } else {
                    binding.coinName.text = "BOUGHT"
                    binding.coinName.setTextColor(Color.BLUE)
                    usdValue = df.format(updatedPrice * amountOfCoins)
                }

                val transactionInformation = "${amountOfCoins} ${coinName} for ${usdValue} USD"
                binding.someTextIdHere2.text = transactionInformation
                binding.someTextIdHere2.setTextColor(Color.BLACK)
                binding.someTextIdHere3.text = dateTime
            }
        }

        fun getEmoji(unicode: Int): String {
            return String(Character.toChars(unicode))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(FragmentTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(transactionList[position])
    }

    override fun getItemCount(): Int {
        return transactionList.size
    }

    fun setTransactionList(list: List<Transactions>) {
        transactionList.clear()
        transactionList.addAll(list)
        notifyDataSetChanged()
    }
}
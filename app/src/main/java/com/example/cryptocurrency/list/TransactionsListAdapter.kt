package com.example.cryptocurrency.list

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cryptocurrency.databinding.FragmentTransactionBinding
import com.example.cryptocurrency.entities.Transactions
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
            val amount : Float = transaction.amountOfCoin
            val coinName : String = transaction.coinName
            val updatedPrice : Float = transaction.updatedPrice
            val dateTime : String = transaction.transactionDate
            Log.d("Coin Name = ", coinName)

            fun getEmoji(unicode: Int): String {
                return String(Character.toChars(unicode))
            }
            if(coinName == "usd"){
                binding.coinName.text = "Installation Reward ${getEmoji(0x1F4B0)} ${getEmoji(0x1F911)}"
                binding.coinName.setTextColor(Color.RED)
                binding.someTextIdHere2.text = "10000 $"
                binding.someTextIdHere2.setTextColor(Color.GREEN)
                binding.someTextIdHere3.text = dateTime
            } else{
                val df = DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH))
                df.maximumFractionDigits = 2
                val usdValueBoughtFor = df.format(updatedPrice*amount)
                val transactionInformation = "${amount} ${coinName} for ${usdValueBoughtFor} USD"

                binding.coinName.text = if (amount < 0)  "SOLD" else "BOUGHT"
                binding.coinName.setTextColor(if (amount < 0)  Color.RED else Color.BLUE)
                binding.someTextIdHere2.text = transactionInformation
                binding.someTextIdHere3.text = dateTime
            }
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
package com.example.cryptocurrency.list

import android.graphics.Color
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

            val df = DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH))
            df.maximumFractionDigits = 2
            val usdValueBoughtFor = df.format(updatedPrice*amount)
            val transactionInformation = "${amount} ${coinName} for ${usdValueBoughtFor} USD"

            //binding.someTextIdHere.text = "BOUGHT"
            binding.someTextIdHere.text = if (amount < 0)  "SOLD" else "BOUGHT"
            binding.someTextIdHere.setTextColor(if (amount < 0)  Color.RED else Color.BLUE)

            binding.someTextIdHere2.text = transactionInformation
            // Date time
            binding.someTextIdHere3.text = dateTime
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
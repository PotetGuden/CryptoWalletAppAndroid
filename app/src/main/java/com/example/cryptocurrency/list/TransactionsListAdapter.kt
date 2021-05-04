package com.example.cryptocurrency.list

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cryptocurrency.databinding.FragmentTransactionBinding
import com.example.cryptocurrency.entities.Transactions
import java.util.*

class TransactionsListAdapter() : RecyclerView.Adapter<TransactionsListAdapter.ViewHolder>(){

    private val transactionList = mutableListOf<Transactions>()

    class ViewHolder(val binding: FragmentTransactionBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(transaction: Transactions) {
            Glide.with(this.itemView).load("https://static.coincap.io/assets/icons/${transaction.coinName.toLowerCase()}@2x.png").into(
                binding.someImgNameHere
            )

            val amount : String = transaction.amountOfCoin.toString()
            val coinName : String = transaction.coinName
            val updatedPrice : String = transaction.updatedPrice.toString()

            val transactionInformation = "${amount} ${coinName} for ${updatedPrice} USD"

            //binding.someTextIdHere.text = "BOUGHT"
            binding.someTextIdHere.text = if (amount[0].toString() == "-")  "SOLD" else "BOUGHT"
            binding.someTextIdHere.setTextColor(if (amount[0].toString() == "-")  Color.RED else Color.BLUE)

            binding.someTextIdHere2.text = transactionInformation
            // Date time
            binding.someTextIdHere3.text = Calendar.getInstance().time.toString()
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
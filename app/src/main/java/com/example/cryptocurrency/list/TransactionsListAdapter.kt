package com.example.cryptocurrency.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cryptocurrency.R
import com.example.cryptocurrency.databinding.FragmentListBinding
import com.example.cryptocurrency.databinding.FragmentTransactionBinding
import com.example.cryptocurrency.entities.Transactions

class TransactionsListAdapter() : RecyclerView.Adapter<TransactionsListAdapter.ViewHolder>(){

    private val transactionList = mutableListOf<Transactions>()

    class ViewHolder(val binding: FragmentTransactionBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(transaction: Transactions) {
            // Image
            Glide.with(this.itemView).load("https://static.coincap.io/assets/icons/${transaction.coinName.toLowerCase()}@2x.png").into(
                binding.someImgNameHere
            )

            /*val textview = activity!!.findViewById<View>(R.id.user_balance) as TextView
            textview.text = "NEW BALANCE"*/

            // Sold/Bought
            binding.someTextIdHere.text = "BOUGHT"
            val amount : String = transaction.amountOfCoin.toString()
            val coinName : String = transaction.coinName
            val updatedPrice : String = transaction.updatedPrice.toString()
            val transactionInformation = "${amount} ${coinName} for ${updatedPrice} USD"
            binding.someTextIdHere2.text = transactionInformation
            // Date time
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
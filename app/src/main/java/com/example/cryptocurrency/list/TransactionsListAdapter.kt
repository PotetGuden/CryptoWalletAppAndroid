package com.example.cryptocurrency.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager

import androidx.recyclerview.widget.RecyclerView
import com.example.cryptocurrency.R
import com.example.cryptocurrency.databinding.FragmentListBinding
import com.example.cryptocurrency.databinding.FragmentTransactionBinding
import com.example.cryptocurrency.entities.Transactions

class TransactionsListAdapter(val lambdaFunction: (Transactions) -> Unit) : RecyclerView.Adapter<TransactionsListAdapter.ViewHolder>(){

    private val transactionList = mutableListOf<Transactions>()

    class ViewHolder(val binding: FragmentTransactionBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(transaction: Transactions) {
            // Sold/Bought
            val amount : String = transaction.amountOfCoin.toString()
            val coinName : String = transaction.coinName
            val updatedPrice : String = transaction.updatedPrice.toString()
            val transactionInformation = "${amount} ${coinName} for ${updatedPrice} USD"
            binding.someTextIdHere2.text = transactionInformation
            // Date time
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val holder = ViewHolder(FragmentTransactionBinding.inflate(LayoutInflater.from(parent.context)))
        holder.itemView.setOnClickListener {
            lambdaFunction(transactionList[holder.adapterPosition])
        }
        return holder
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
package com.example.cryptocurrency.list

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptocurrency.R
import com.example.cryptocurrency.databinding.FragmentTransactionsListBinding
import com.example.cryptocurrency.entities.Transactions


class TransactionsListFragment() : Fragment(R.layout.fragment_transactions_list) {

    private val adapter = TransactionsListAdapter()

    private val viewModel: TransactionsListViewModel by lazy {
        ViewModelProvider(this).get(TransactionsListViewModel::class.java)
    }

    private lateinit var binding: FragmentTransactionsListBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTransactionsListBinding.bind(view)
        viewModel.init(requireContext())
        viewModel.fetchAllData()
        binding.transactionsList.layoutManager = LinearLayoutManager(requireContext())
        binding.transactionsList.adapter =  adapter //TransactionsListAdapter() // eller bare adapter

        viewModel.transactionListLiveData.observe(viewLifecycleOwner){
            adapter.setTransactionList(it)
        }
    }
}
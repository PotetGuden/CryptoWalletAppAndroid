package com.example.cryptocurrency.details

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptocurrency.MainViewModel
import com.example.cryptocurrency.R
import com.example.cryptocurrency.databinding.FragmentPortofolioBinding
import com.example.cryptocurrency.list.TransactionsListFragment
import com.example.cryptocurrency.list.TransactionsListViewModel

class PortofolioFragment : Fragment(R.layout.fragment_portofolio){

    private val databaseTransactionViewModel: TransactionsListViewModel by viewModels()
    private val apiListViewModel: MainViewModel by viewModels()

    private val adapter = PortofolioItemAdapter()

    private lateinit var binding: FragmentPortofolioBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPortofolioBinding.bind(view)

        binding.transactionBtn.setOnClickListener{
            parentFragmentManager.beginTransaction().apply{
                replace(
                    R.id.container_for_transaction,
                    TransactionsListFragment(), "TransactionListFragment"
                )
                    .addToBackStack("Transactions")
                    .commit()
            }
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        // Fetching and setting DB and cryptoAPI to adapter
        databaseTransactionViewModel.init(requireContext())
        databaseTransactionViewModel.fetchTransactionsGrouped()
        databaseTransactionViewModel.transactionListGroupedLiveData.observe(viewLifecycleOwner){
            adapter.setTransactionList(it)
        }

        apiListViewModel.LoadCoinFromList()
        apiListViewModel.allCurrencies.observe(viewLifecycleOwner){ currencies ->
            adapter.setUpdatedPriceList(currencies.data)
        }
    }

    override fun onResume() {   // headerTitle.onClick = false?
        super.onResume()
        apiListViewModel.LoadCoinFromList()
        databaseTransactionViewModel.fetchTransactionsGrouped()
    }
}

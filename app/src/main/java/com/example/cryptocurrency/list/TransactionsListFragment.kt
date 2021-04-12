package com.example.cryptocurrency.list

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptocurrency.MainActivity
import com.example.cryptocurrency.MainViewModel
import com.example.cryptocurrency.R
import com.example.cryptocurrency.databinding.FragmentListBinding
import com.example.cryptocurrency.databinding.FragmentTransactionsListBinding
import com.example.cryptocurrency.details.TransactionFragment


class TransactionsListFragment : Fragment(R.layout.fragment_transactions_list) {

    private val adapter = TransactionsListAdapter { transaction ->   // Han brukte parentFragmentManager
        fragmentManager?.beginTransaction()?.apply {
            replace(R.id.currency_fragment_container, TransactionFragment.newInstance(transactionID = transaction.transactionsId))
                .addToBackStack("StudentFragment")
                .commit()

        }

        //setTextViewText

    }

    private val viewModel: TransactionsListViewModel by lazy {
        ViewModelProvider(this).get(TransactionsListViewModel::class.java)
    }

    private lateinit var binding: FragmentTransactionsListBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTransactionsListBinding.bind(view)
        viewModel.init(requireContext())
        binding.transactionsList.layoutManager = LinearLayoutManager(requireContext())
        binding.transactionsList.adapter =  adapter // TransactionsListAdapter() // eller bare adapter
        var balance : Float = 0.0F

        viewModel.transactionListLiveData.observe(viewLifecycleOwner){
            adapter.setTransactionList(it)
            balance += it[0].updatedPrice*it[0].amountOfCoin
            val balanceText = activity!!.findViewById<View>(R.id.user_balance) as TextView
            balanceText.text = "Balance: ${balance.toString()}$"
        }


        // WTFFF
        /*viewModel.transactionListLiveData.observe(this){ currencies ->
            binding.transactionsList.adapter = TransactionsListAdapter(){
                fragmentManager?.beginTransaction()?.apply{
                    replace(R.id.currency_fragment_container, TransactionFragment.newInstance())
                        .addToBackStack("Currency")
                        .commit()
                }
            }
        }*/
    }

}
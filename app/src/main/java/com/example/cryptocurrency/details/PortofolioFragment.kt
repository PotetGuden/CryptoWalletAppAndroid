package com.example.cryptocurrency.details

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptocurrency.MainViewModel
import com.example.cryptocurrency.R
import com.example.cryptocurrency.databinding.FragmentPortofolioBinding
import com.example.cryptocurrency.list.TransactionsListFragment
import com.example.cryptocurrency.list.TransactionsListViewModel
import java.util.*

class PortofolioFragment : Fragment(R.layout.fragment_portofolio){

    // DATABASE
    private val databaseTransactionViewModel: TransactionsListViewModel by lazy {
        ViewModelProvider(this).get(TransactionsListViewModel::class.java)
    }
    //private val viewModel : TransactionsListViewModel by viewModels()


    // Adapter tar seg av hva som blir printet ut i recyclerview
    //private val adapter = PortofolioItemAdapter()

    //private var balanceUsd = getUsdBalance()

    private val adapter = PortofolioItemAdapter()
    // API
    private val apiListViewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }
    /*private fun getUsdBalance(): Float {
        // Sending balance as USD
        var balance = 0F
        viewModel.init(requireContext())
        viewModel.fetchAllData()
        viewModel.transactionListLiveData.observe(this){
            balanceUsd = 0F
            if(it.isEmpty()){
                Log.d("Database", "is empty!")
            } else{
                for (i in it.indices) {
                    balanceUsd += it[i].updatedPrice*it[i].amountOfCoin
                }
                //adapter.
            }
        }
        Log.d("Balance USD", balance.toString())
        return balance
    }*/

    private lateinit var binding: FragmentPortofolioBinding // Portofolio forside xml

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPortofolioBinding.bind(view)

        //binding.textView2.text = "hmm" // SETT TEKST FOR PORTOFOLIO FORSIDE HER

        // Transaction button onclick
        binding.transactionBtn.setOnClickListener{
            parentFragmentManager.beginTransaction().apply{
                replace(R.id.main_container,
                    TransactionsListFragment(),"TransactionListFragment")
                    .addToBackStack("Transactions")
                    .commit()
            }
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        // Henter DB data
        databaseTransactionViewModel.init(requireContext())
        databaseTransactionViewModel.fetchAllData() // Trenger nok ikke denne
        databaseTransactionViewModel.fetchTotalAmountOfCoinsPerCoin()
        databaseTransactionViewModel.fetchNameAmountOfCoins()


        // CoinName sorted
        databaseTransactionViewModel.sumAmountCoinNameListLiveData.observe(viewLifecycleOwner){
            adapter.setCoinNameList(it)
        }
        // Amount of coins per coin
        databaseTransactionViewModel.sumAmountOfCoinsListLiveData.observe(viewLifecycleOwner){
            adapter.setAmountOfCoinsList(it)
        }
        // Transaction List  - trenger nok ikke denne
        databaseTransactionViewModel.transactionListLiveData.observe(viewLifecycleOwner){
            adapter.setTransactionList(it)
        }

        apiListViewModel.allCurrencies.observe(viewLifecycleOwner){ currencies ->
            adapter.setUpdatedPriceList(currencies.data)
        }
    }
}

package com.example.cryptocurrency.list

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptocurrency.viewModels.ApiViewModel
import com.example.cryptocurrency.PurchaseActivity
import com.example.cryptocurrency.R
import com.example.cryptocurrency.adapters.CurrencyListAdapter
import com.example.cryptocurrency.databinding.FragmentListBinding
import com.example.cryptocurrency.viewModels.TransactionsListViewModel


class CurrencyListFragment() : Fragment(R.layout.fragment_list) {

    private val currencyListViewModel: ApiViewModel by lazy {
        ViewModelProvider(this).get(ApiViewModel::class.java)
    }

    private val viewModel: TransactionsListViewModel by lazy {
        ViewModelProvider(this).get(TransactionsListViewModel::class.java)
    }

    // currencyListViewModel . UPDATE FUNKSJON HER??=??

    private lateinit var binding: FragmentListBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        binding = FragmentListBinding.bind(view)

        viewModel.init(requireContext())
        viewModel.fetchAllData()

        binding.currencyList.layoutManager = LinearLayoutManager(requireContext())

        currencyListViewModel.allCurrencies.observe(viewLifecycleOwner){ currencies ->
            // for å liste ut alle currencies
            binding.currencyList.adapter = CurrencyListAdapter(currencies){
                // for onclick
                parentFragmentManager.beginTransaction().apply{
                    val coinName = it.symbol
                    var amountOfCoins = 0F
                    viewModel.transactionListLiveData.observe(viewLifecycleOwner){
                        // alle transaksjoner
                        for(transaction in it){
                            if(coinName == transaction.coinName){
                                amountOfCoins += transaction.amountOfCoin
                            }
                        }
                    }
                    val intent = Intent(activity, PurchaseActivity::class.java)
                    val imageString = "https://static.coincap.io/assets/icons/${it.symbol.toLowerCase()}@2x.png"
                    intent.putExtra("imageString", imageString)
                    intent.putExtra("coinName", it.name)
                    intent.putExtra("coinSymbol", it.symbol)
                    intent.putExtra("coinPrice", it.priceUsd)
                    intent.putExtra("coinId", it.id)

                    startActivity(intent)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        currencyListViewModel.LoadCoinFromList()
    }

}
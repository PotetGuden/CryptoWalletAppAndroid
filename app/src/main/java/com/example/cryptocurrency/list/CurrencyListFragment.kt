package com.example.cryptocurrency.list

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.cryptocurrency.MainViewModel
import com.example.cryptocurrency.R
import com.example.cryptocurrency.databinding.FragmentListBinding


class CurrencyListFragment() : Fragment(R.layout.fragment_list) {

    private val currencyListViewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    private val viewModel: TransactionsListViewModel by lazy {
        ViewModelProvider(this).get(TransactionsListViewModel::class.java)
    }

    private lateinit var binding: FragmentListBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        binding = FragmentListBinding.bind(view)

        viewModel.init(requireContext())
        viewModel.fetchAllData()

        binding.currencyList.layoutManager = LinearLayoutManager(requireContext())
        currencyListViewModel.allCurrencies.observe(this){ currencies ->
            // for å liste ut alle currencies
            binding.currencyList.adapter = CurrencyListAdapter(currencies){

                // for onclick
                fragmentManager?.beginTransaction()?.apply{
                    var coinName = it.symbol
                    var amountOfCoins = 0F
                    viewModel.transactionListLiveData.observe(viewLifecycleOwner){
                        // alle transaksjoner
                        for(transaction in it){
                            if(coinName == transaction.coinName){
                                amountOfCoins += transaction.amountOfCoin
                            }
                        }
                    }

                    replace(R.id.currency_fragment_container, CurrencyFragment.newInstance(it, amountOfCoins))
                            .addToBackStack("Currency")
                            .commit()
                }
            }
        }


    }
}
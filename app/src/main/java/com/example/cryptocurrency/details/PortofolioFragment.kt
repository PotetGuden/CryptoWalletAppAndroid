package com.example.cryptocurrency.details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptocurrency.MainViewModel
import com.example.cryptocurrency.R
import com.example.cryptocurrency.databinding.FragmentPortofolioBinding
import com.example.cryptocurrency.list.TransactionsListViewModel

class PortofolioFragment : Fragment(R.layout.fragment_portofolio){


    private val adapter = PortofolioItemAdapter()

    private val viewModel: TransactionsListViewModel by lazy {
        ViewModelProvider(this).get(TransactionsListViewModel::class.java)
    }

    private val currencyListViewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }
    private lateinit var binding: FragmentPortofolioBinding // xml
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPortofolioBinding.bind(view)

        //binding.textView2.text = "hmm" // SETT TEKST FOR PORTOFOLIO FORSIDE HER

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
        // Henter DB
        viewModel.init(requireContext())
        viewModel.fetchDataByCurrencySymbolName("BTC")
        //viewModel.fetchAllData()
        //viewModel.fetchTotalAmountOfCoinsPerCoin()
        viewModel.transactionListLiveData.observe(viewLifecycleOwner){
            adapter.setTransactionList(it)
        }

        currencyListViewModel.allCurrencies.observe(this){ currencies ->
            for(i in currencies.data.indices){
                if(currencies.data[i].name == "lol"){

                }
            }
        }







        //viewModel.init(requireContext())

        // FOR Å SENDE MEG TING?

        /*viewModel.transactionLiveData.observe(this){ currencies ->
            fragmentManager?.beginTransaction()?.apply {
                replace(R.id.recyclerView, PortofolioItemFragment())
                    .addToBackStack("PortofolioItemFragment")
                    .commit()
            }
        }*/
        //}
        /*currencyListViewModel.allCurrencies.observe(this){ currencies ->
            binding.currencyList.adapter = CurrencyListAdapter(currencies){
                fragmentManager?.beginTransaction()?.apply{
                    replace(R.id.currency_fragment_container, CurrencyFragment.newInstance(it))
                            .addToBackStack("Currency")
                            .commit()
                }
            }
        }*/
    }
}

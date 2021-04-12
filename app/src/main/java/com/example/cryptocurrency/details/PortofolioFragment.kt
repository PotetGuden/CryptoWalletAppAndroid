package com.example.cryptocurrency.details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptocurrency.MainViewModel
import com.example.cryptocurrency.R
import com.example.cryptocurrency.databinding.FragmentListBinding
import com.example.cryptocurrency.databinding.FragmentPortofolioBinding
import com.example.cryptocurrency.list.CurrencyFragment
import com.example.cryptocurrency.list.CurrencyListAdapter
import com.example.cryptocurrency.list.TransactionsListViewModel

class PortofolioFragment : Fragment(R.layout.fragment_portofolio){

    private val viewModel: TransactionsListViewModel by lazy {
        ViewModelProvider(this).get(TransactionsListViewModel::class.java)
    }

    private lateinit var binding: FragmentPortofolioBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPortofolioBinding.bind(view)


        /*binding.currencyList.layoutManager = LinearLayoutManager(requireContext())
        viewModel.transactionListLiveData.observe(this){ currencies ->
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
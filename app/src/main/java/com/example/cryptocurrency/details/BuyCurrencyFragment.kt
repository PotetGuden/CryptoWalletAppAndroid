package com.example.cryptocurrency.details

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asFlow
import androidx.room.Transaction
import com.bumptech.glide.Glide
import com.example.cryptocurrency.R
import com.example.cryptocurrency.databinding.FragmentBuyCurrencyBinding
import com.example.cryptocurrency.entities.Transactions
import com.example.cryptocurrency.list.CurrencyListFragment
import com.example.cryptocurrency.list.TransactionsListFragment

class BuyCurrencyFragment : Fragment(R.layout.fragment_buy_currency){

    private var transactionsID: Long? = null
    private lateinit var binding: FragmentBuyCurrencyBinding
    private val  viewModel: TransactionsViewModel by lazy(){
        ViewModelProvider(this).get(TransactionsViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBuyCurrencyBinding.bind(view)
        transactionsID = arguments?.getLong("transactionsID")

        viewModel.init(requireContext())

        val imgName : String? = arguments?.getString("imgName")
        val coinName : String? = arguments?.getString("coinName")
        val coinSymbol : String? = arguments?.getString("coinSymbol")
        val coinPrice : String? = arguments?.getString("coinPrice")



        val correctPriceFormat: String = "$" + coinPrice?.substring(0,coinPrice.indexOf(".")+3)

        if(imgName == null || coinName == null || coinSymbol == null || coinPrice == null ){

        } else{

            initViewListeners(coinName, coinPrice)
            Glide.with(this).load(imgName).into(
                binding.someImgNameHere
            )
            binding.someTextIdHere.text = coinName
            binding.someTextIdHere2.text = coinSymbol
            binding.someTextIdHere3.text = correctPriceFormat
            //binding.someTextIdHere4.text = correctPercentChangeFormat
        }
    }

    companion object { // static function - har tilang til arguments som man sender til newInstance()
        fun newInstance(imgName: String?, coinName: String?, coinSymbol: String?, coinPrice: String?,): BuyCurrencyFragment = BuyCurrencyFragment().apply{
            arguments = Bundle().apply{
                //val imageString = "https://static.coincap.io/assets/icons/${coin.symbol.toLowerCase()}@2x.png"

                putString("imgName", imgName)
                putString("coinSymbol", coinName)
                putString("coinName", coinSymbol)
                putString("coinPrice", coinPrice)

            }
        }
    }

    private fun initViewListeners(coinName: String, coinPrice: String){
        with(binding){
            button.setOnClickListener{
                val amountOfCoins = editText.text.toString().toFloat()
                viewModel.save(coinName,coinPrice.toFloat(),amountOfCoins)

                fragmentManager?.beginTransaction()?.apply {
                    replace(R.id.currency_fragment_container,
                    TransactionsListFragment(),"TransactionListFragment")
                    .commit()
                }

                viewModel.transactionLiveData.observe(viewLifecycleOwner){
                    var balance : Float = it.amountOfCoin*it.updatedPrice
                    val balanceText = activity!!.findViewById<View>(R.id.user_balance) as TextView
                    balanceText.text = balance.toString()
                }
                /*val balanceText = activity!!.findViewById<View>(R.id.user_balance) as TextView
                balanceText.text = "NEW BALANCE"*/
            }
        }
    }

}
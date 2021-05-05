package com.example.cryptocurrency.details

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.cryptocurrency.MainViewModel
import com.example.cryptocurrency.R
import com.example.cryptocurrency.databinding.FragmentBuyCurrencyBinding
import com.example.cryptocurrency.databinding.FragmentSellCurrencyBinding
import com.example.cryptocurrency.list.TransactionsListViewModel
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

class SellCurrencyFragment : Fragment(R.layout.fragment_sell_currency) {
    private var transactionsID: Long? = null
    private lateinit var binding: FragmentSellCurrencyBinding
    private val viewModel: TransactionsViewModel by lazy(){
        ViewModelProvider(this).get(TransactionsViewModel::class.java)
    }

    private val apiViewModel: MainViewModel by viewModels()
    private val dbViewModel: TransactionsListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSellCurrencyBinding.bind(view)
        transactionsID = arguments?.getLong("transactionsID")

        viewModel.init(requireContext())

        val imgName : String? = arguments?.getString("imgName")
        val coinSymbol : String? = arguments?.getString("coinSymbol")
        val coinPrice : String? = arguments?.getString("coinPrice")
        val coinId: String? = arguments?.getString("coinId")

        if(imgName == null || coinSymbol == null || coinPrice == null || coinId == null ){

        } else{
            dbViewModel.init(requireContext())
            dbViewModel.fetchAmountOfCoinsByName(coinSymbol)
            // Refreshing Amount of coins
            var amountOfCoin = 0F
            dbViewModel.sumAmountOfCoinsByNameLiveData.observe(viewLifecycleOwner){  amountOfCoins ->
                amountOfCoin = amountOfCoins
                binding.balanceMessage.text = "You can only sell cryptocurrency in USD\n\nYou have ${amountOfCoins} ${coinSymbol}"
            }

            initViewListeners(coinId, coinSymbol, coinPrice)
            binding.sellButton.text = "SELL"
            binding.coinSymbol.text = coinSymbol
            //binding.balanceMessage.text = "You can only sell cryptocurrency in USD\n\nYou have ${amountOfCoins} ${coinSymbol}"
            //binding.someTextIdHere4.text = correctPercentChangeFormat*/
            binding.editText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {}
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    apiViewModel.LoadCoinByName(coinId)
                    apiViewModel.specificCoin.observe(viewLifecycleOwner){
                        if(s.toString() != "") {
                            binding.sellButton.isEnabled = s.toString().toFloat() <= amountOfCoin

                            val usdAmount = s.toString().toDouble() * coinPrice.toDouble()
                            val df = DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH))
                            df.maximumFractionDigits = 2

                            binding.editText2.text = df.format(usdAmount)
                            Log.d("usdAMOUNT", usdAmount.toString())
                        } else{
                            binding.editText2.text = "" // hmm
                        }
                    }
                }
            })
        }
    }

    companion object { // static function - har tilgang til arguments som man sender til newInstance()
        fun newInstance(imgName: String?, coinName: String?, coinSymbol: String?, coinPrice: String?, amountOfCoins: Float, coinId: String?): SellCurrencyFragment = SellCurrencyFragment().apply{
            arguments = Bundle().apply{
                putString("imgName", imgName)
                putString("coinSymbol", coinSymbol)
                putString("coinPrice", coinPrice)
                putFloat("amountOfCoins", amountOfCoins)
                putString("coinId", coinId)
            }
        }
    }

    private fun initViewListeners(coinId: String, coinName: String, coinPrice: String){
        with(binding){
            sellButton.setOnClickListener{
                val amountOfCoins = editText.text.toString().toFloat() * -1F
                //val amountOfUsd = editText2.text.toString().toFloat()
                viewModel.save(coinId, coinName, coinPrice.toFloat(), amountOfCoins)
                parentFragmentManager.popBackStack()
            }
        }
    }
}
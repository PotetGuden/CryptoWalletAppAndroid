package com.example.cryptocurrency.details

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.cryptocurrency.MainViewModel
import com.example.cryptocurrency.R
import com.example.cryptocurrency.databinding.FragmentSellCurrencyBinding
import com.example.cryptocurrency.list.TransactionsListViewModel
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

class SellCurrencyFragment : Fragment(R.layout.fragment_sell_currency) {
    private lateinit var binding: FragmentSellCurrencyBinding
    private val viewModel: TransactionsViewModel by viewModels()

    private val apiViewModel: MainViewModel by viewModels()
    private val dbViewModel: TransactionsListViewModel by viewModels()

    private var transactionsID: Long? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSellCurrencyBinding.bind(view)
        transactionsID = arguments?.getLong("transactionsID")

        viewModel.init(requireContext())

        val coinSymbol : String? = arguments?.getString("coinSymbol")
        val coinPrice : String? = arguments?.getString("coinPrice")
        val coinId: String? = arguments?.getString("coinId")

        if( coinSymbol == null || coinPrice == null || coinId == null ){
            // Show some error if wanted
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

    companion object {
        fun newInstance(coinSymbol: String?, coinPrice: String?, coinId: String?): SellCurrencyFragment = SellCurrencyFragment().apply{
            arguments = Bundle().apply{
                putString("coinSymbol", coinSymbol)
                putString("coinPrice", coinPrice)
                putString("coinId", coinId)
            }
        }
    }

    private fun initViewListeners(coinId: String, coinName: String, coinPrice: String){
        with(binding){
            sellButton.setOnClickListener{
                val amountOfCoins = editText.text.toString().toFloat() * -1F
                viewModel.save(coinId, coinName, coinPrice.toFloat(), amountOfCoins)
                parentFragmentManager.popBackStack()
            }
        }
    }
}
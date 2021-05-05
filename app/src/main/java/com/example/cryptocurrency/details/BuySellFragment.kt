package com.example.cryptocurrency.details

import android.os.Bundle
import android.text.TextUtils.replace
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.cryptocurrency.MainViewModel
import com.example.cryptocurrency.R
import com.example.cryptocurrency.databinding.FragmentCurrencyBinding
import com.example.cryptocurrency.databinding.FragmentPortofolioBinding
import com.example.cryptocurrency.entities.Transactions
import com.example.cryptocurrency.list.TransactionsListViewModel
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import kotlin.math.absoluteValue

class BuySellFragment() : Fragment(R.layout.fragment_currency){

    private lateinit var binding: FragmentCurrencyBinding

    private val viewModel: TransactionsListViewModel by viewModels()
    private val currencyListViewModel: MainViewModel by viewModels()

    private fun updateScreen(imageString: String, coinSymbol: String, coinName: String, coinPrice: String, coinId: String){
        currencyListViewModel.LoadCoinByName(coinId)
        var updatedPrice = coinPrice
        currencyListViewModel.specificCoin.observe(viewLifecycleOwner){
            updatedPrice = it.data.priceUsd
        }

        val correctPriceFormat: String = "$" + updatedPrice.substring(0, coinPrice.indexOf(".") + 3)

        Glide.with(this).load(imageString).into(
            binding.imageId
        )
        binding.coinName.text = coinName
        binding.coinSymbol.text = coinSymbol

        currencyListViewModel.allCurrencies.observe(viewLifecycleOwner){
            for(i in it.data){
                if(i.id == coinId){
                    val priceFormatted = BigDecimal(i.priceUsd.toDouble()).setScale(2,RoundingMode.HALF_EVEN)
                    binding.updatedPrice.text = "$${priceFormatted.toString()}"
                }
            }
        }
        viewModel.init(requireContext())
        viewModel.fetchAmountOfCoinsByName(coinSymbol)

        // Refreshing Amount of coins
        viewModel.sumAmountOfCoinsByNameLiveData.observe(viewLifecycleOwner){ amountOfCoins ->
            binding.sellButton.isEnabled = amountOfCoins != 0F

            val value: Float = amountOfCoins * updatedPrice.toFloat()
            val valueFormatted =  BigDecimal(value.toDouble()).setScale(2, RoundingMode.HALF_EVEN)
            val amountOfCoinsFormatted = if( amountOfCoins.absoluteValue % 1.0 <= 0.01){
                BigDecimal(amountOfCoins.toDouble()).setScale(4, RoundingMode.HALF_EVEN)    // If the amount has more then 2 zero's (0.00xx)
            } else{
                BigDecimal(amountOfCoins.toDouble()).setScale(2, RoundingMode.HALF_EVEN)
            }
            binding.balanceMessage.text = "You have ${amountOfCoinsFormatted} ${coinSymbol}\n${amountOfCoinsFormatted} x ${correctPriceFormat}\nValue ${valueFormatted} USD"
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCurrencyBinding.bind(view)

        val imageString: String? = arguments?.getString("imageString")
        val coinId: String? = arguments?.getString("coinId")
        val coinSymbol: String? = arguments?.getString("coinSymbol")
        val coinName: String? = arguments?.getString("coinName")
        val coinPrice: String? = arguments?.getString("coinPrice")
        val amountOfCoins: Float = requireArguments().getFloat("amountOfCoins",0F)

        if(imageString == null || coinName == null || coinSymbol == null || coinPrice == null || coinId == null){
            showError()
        } else {

            updateScreen(imageString,coinSymbol,coinName,coinPrice, coinId)

            binding.buyButton.setOnClickListener{
                parentFragmentManager.beginTransaction().apply{
                    replace(R.id.buySellContainer, BuyCurrencyFragment.newInstance(imageString, coinId, coinName, coinSymbol, coinPrice, amountOfCoins))
                        .addToBackStack("Currency")
                        .commit()
                }
            }

            binding.sellButton.setOnClickListener{
                parentFragmentManager.beginTransaction().apply{
                    replace(R.id.buySellContainer, SellCurrencyFragment.newInstance(imageString,coinName,coinSymbol,coinPrice, amountOfCoins, coinId)) // Trenger ikke coinName for sell
                        .addToBackStack("Currency")
                        .commit()
                }
            }
            parentFragmentManager.addOnBackStackChangedListener {
                currencyListViewModel.LoadCoinFromList()
                updateScreen(imageString,coinSymbol,coinName,coinPrice, coinId)
                Log.d("PurchaseActivity", "onBackStackListener")
            }
        }
    }

    private fun showError() {
        Log.d("ERROR", "Errorrrr")
    }

    companion object { // static function - har tilgang til arguments som man sender til newInstance()
        fun newInstance(imgName: String?, coinName: String?, coinSymbol: String?, coinPrice: String?, amountOfCoins: Float, coinId: String?): BuySellFragment = BuySellFragment().apply{
            arguments = Bundle().apply{
                putString("imageString", imgName)
                putString("coinName", coinName)
                putString("coinSymbol", coinSymbol)
                putString("coinPrice", coinPrice)
                putFloat("amountOfCoins", amountOfCoins)
                putString("coinId", coinId)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        //currencyListViewModel.LoadCoinFromList()
        Log.d("BuySellFragment", "onResume")
    }
}
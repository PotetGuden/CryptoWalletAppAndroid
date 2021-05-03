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

class BuySellFragment() : Fragment(R.layout.fragment_currency){

    private lateinit var binding: FragmentCurrencyBinding
    override fun onResume() {
        super.onResume()

        Log.d("BuySellFragment", "onResume")
    }


    private val viewModel: TransactionsListViewModel by viewModels()
    private val currencyListViewModel: MainViewModel by viewModels()

    private fun test(imageString: String, coinSymbol: String, coinName: String, coinPrice: String, coinId: String){
        currencyListViewModel.LoadCoinByName(coinId)
        var updatedPrice = coinPrice
        currencyListViewModel.specificCoin.observe(viewLifecycleOwner){
            updatedPrice = it.data.priceUsd
        }

        val correctPriceFormat: String = "$" + updatedPrice.substring(0, coinPrice.indexOf(".") + 3)

        Glide.with(this).load(imageString).into(
            binding.someImgNameHere
        )
        binding.someTextIdHere.text = coinName
        binding.someTextIdHere2.text = coinSymbol
        binding.someTextIdHere3.text = correctPriceFormat

        viewModel.init(requireContext())
        viewModel.fetchAmountOfCoinsByName(coinSymbol)
        // Refreshing Amount of coins
        viewModel.sumAmountOfCoinsByNameLiveData.observe(viewLifecycleOwner){ amountOfCoins ->
            val value: Float = amountOfCoins * updatedPrice.toFloat()
            Log.d("Amount of Coins", amountOfCoins.toString())
            binding.someTextIdHere5.text = "You have ${amountOfCoins} ${coinSymbol}\n${amountOfCoins} x ${correctPriceFormat}\nValue ${value} USD"
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //binding = FragmentCurrencyBinding.inflate(layoutInflater)
        binding = FragmentCurrencyBinding.bind(view)
        //setContentView(binding.root)

        val imageString: String? = arguments?.getString("imageString")
        val coinId: String? = arguments?.getString("coinId")
        val coinSymbol: String? = arguments?.getString("coinSymbol")
        val coinName: String? = arguments?.getString("coinName")
        val coinPrice: String? = arguments?.getString("coinPrice")
        val amountOfCoins: Float = requireArguments().getFloat("amountOfCoins",0F)

        if(imageString == null || coinName == null || coinSymbol == null || coinPrice == null || coinId == null){
            showError()
        } else {

            test(imageString,coinSymbol,coinName,coinPrice, coinId)
            Log.d("ImageString", imageString.toString())
            Log.d("Coin Symbol", coinSymbol.toString())
            Log.d("Coin Name", coinName.toString())
            Log.d("Coin Id", coinId.toString())
            /*binding.button3.setOnClickListener{
                test(imageString,coinSymbol,coinName,coinPrice, coinId)
            }*/

            binding.button.setOnClickListener{
                parentFragmentManager.beginTransaction().apply{
                    replace(R.id.purchase_fragment_container, BuyCurrencyFragment.newInstance(imageString,coinName,coinSymbol,coinPrice, amountOfCoins))
                        .addToBackStack("Currency")
                        .commit()
                }
            }

            binding.button2.setOnClickListener{
                parentFragmentManager.beginTransaction().apply{
                    replace(R.id.purchase_fragment_container, SellCurrencyFragment.newInstance(imageString,coinName,coinSymbol,coinPrice, amountOfCoins, coinId)) // Trenger ikke coinName for sell
                        .addToBackStack("Currency")
                        .commit()
                }
            }
            parentFragmentManager.addOnBackStackChangedListener {
                test(imageString,coinSymbol,coinName,coinPrice, coinId)
                Log.d("PurchaseActivity", "onBackStackListener")
            }
        }
    }

    private fun showError() {
        Log.d("ERROR", "Errorrrr")
    }
    private val transactionList = mutableListOf<Transactions>()
    fun updateBalance(){
        var balance : Float = 0.0F
        viewModel.fetchAllData()

        viewModel.transactionListLiveData.observe(this){
            setTransactionList(it)
            if(it.isEmpty()){
                Log.d("Database", "is empty!")
                // Kanskje legge til installation reward her?
            } else{
                for (i in it.indices) {
                    balance += it[i].updatedPrice*it[i].amountOfCoin
                    Log.d(i.toString(), "${it[i].updatedPrice}*${it[i].amountOfCoin}")
                }
            }
        }
    }
    fun setTransactionList(list: List<Transactions>) {
        transactionList.clear()
        transactionList.addAll(list)
    }
    companion object { // static function - har tilgang til arguments som man sender til newInstance()
        fun newInstance(imgName: String?, coinName: String?, coinSymbol: String?, coinPrice: String?, amountOfCoins: Float, coinId: String?): BuySellFragment = BuySellFragment().apply{
            arguments = Bundle().apply{
                //val imageString = "https://static.coincap.io/assets/icons/${coin.symbol.toLowerCase()}@2x.png"

                putString("imageString", imgName)
                putString("coinName", coinName)
                putString("coinSymbol", coinSymbol)
                putString("coinPrice", coinPrice)
                putFloat("amountOfCoins", amountOfCoins)
                putString("coinId", coinId)
            }
        }
    }
}
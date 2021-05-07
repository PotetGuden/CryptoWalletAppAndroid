package com.example.cryptocurrency

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.cryptocurrency.databinding.ActivityMainBinding
import com.example.cryptocurrency.fragments.PortofolioFragment
import com.example.cryptocurrency.viewModels.TransactionsViewModel
import com.example.cryptocurrency.list.CurrencyListFragment
import com.example.cryptocurrency.viewModels.TransactionsListViewModel
import com.example.cryptocurrency.viewModels.ApiViewModel
import java.math.BigDecimal
import java.math.RoundingMode


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreference: SharedPreferences

    private val apiViewModel: ApiViewModel by viewModels()
    private val viewModel: TransactionsListViewModel by viewModels()
    private val transactionViewModel: TransactionsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        supportFragmentManager.beginTransaction()
            .replace(
                R.id.currency_fragment_container,
                CurrencyListFragment(), "CurrencyList"
            )
            .commit()

        viewModel.init(this)

        sharedPreference = getSharedPreferences("init-transaction", Context.MODE_PRIVATE)
        val keyValueData = sharedPreference.getBoolean("firstTimeLoggingIn", true)

        if(keyValueData){
            val editor = sharedPreference.edit()
            editor.putBoolean("firstTimeLoggingIn", false)
            editor.apply()
            transactionViewModel.init(this)
            transactionViewModel.save("Dollar","usd", -10000F, 1F)
            updateBalance()
        }

        binding.headerTitle.setOnClickListener{
            supportFragmentManager.beginTransaction()
                .add(
                    R.id.currency_fragment_container,
                    PortofolioFragment(), "xdd"
                )
                    .addToBackStack("Portofolio")
                    .commit()
        }
    }

    override fun onResume() {
        super.onResume()
        updateBalance()
    }

    private fun updateBalance(){
        viewModel.fetchAllData() // hmm
        viewModel.transactionListLiveData.observe(this){    transactionList ->
            apiViewModel.LoadCoinFromList()
            apiViewModel.allCurrencies.observe(this){   allCurrencies ->
                var usdBalance = 0F
                var currencyBalance = 0F
                for(databaseItem in transactionList){
                    if(databaseItem.coinId == "Dollar"){
                        usdBalance += 10000F
                    }
                    for(updatedCurrency in allCurrencies.data){
                        if(databaseItem.coinId == updatedCurrency.id){
                            usdBalance += databaseItem.amountOfCoin * databaseItem.updatedPrice *-1F
                            currencyBalance += databaseItem.amountOfCoin * updatedCurrency.priceUsd.toFloat()
                        }
                    }
                }
                val accountBalance = BigDecimal(usdBalance.toDouble()+currencyBalance.toDouble()).setScale(2,RoundingMode.HALF_EVEN)
                binding.userBalance.text = "Balance: ${accountBalance}$"
            }
        }
    }
}

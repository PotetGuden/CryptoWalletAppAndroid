package com.example.cryptocurrency

import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.cryptocurrency.databinding.FragmentCurrencyBinding
import com.example.cryptocurrency.details.BuyCurrencyFragment
import com.example.cryptocurrency.details.SellCurrencyFragment
import com.example.cryptocurrency.details.TransactionsViewModel
import com.example.cryptocurrency.entities.Transactions
import com.example.cryptocurrency.list.CurrencyFragment
import com.example.cryptocurrency.list.TransactionsListViewModel
import java.util.*

class PurchaseActivity : AppCompatActivity() {

    private lateinit var binding: FragmentCurrencyBinding

    override fun onResume() {
        super.onResume()
        Log.d("Purchase Activity", "onResume")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("Purchase Activity", "onRestart")
    }

    override fun onStart() {
        super.onStart()
        Log.d("Purchase Activity", "onStart")
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        Log.d("Purchase Activity", "onUserInteraction")
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        Log.d("Purchase Activity", "onResumeFragments")
    }

    override fun onPostResume() {
        super.onPostResume()
        Log.d("Purchase Activity", "onPostResume")
    }

    private val viewModel: TransactionsListViewModel by viewModels()
    private val currencyListViewModel: MainViewModel by viewModels()

    private fun test(imageString: String, coinSymbol: String, coinName: String, coinPrice: String, coinId: String){
        currencyListViewModel.LoadCoinByName(coinId)
        var updatedPrice = coinPrice
        currencyListViewModel.specificCoin.observe(this){
            updatedPrice = it.data.priceUsd
        }

        val correctPriceFormat: String = "$" + updatedPrice.substring(0, coinPrice.indexOf(".") + 3)

        Glide.with(this).load(imageString).into(
            binding.someImgNameHere
        )
        binding.someTextIdHere.text = coinName
        binding.someTextIdHere2.text = coinSymbol
        binding.someTextIdHere3.text = correctPriceFormat

        viewModel.init(this)
        viewModel.fetchAmountOfCoinsByName(coinSymbol)
        // Refreshing Amount of coins
        viewModel.sumAmountOfCoinsByNameLiveData.observe(this){ amountOfCoins ->
            val value: Float = amountOfCoins * updatedPrice.toFloat()
            binding.someTextIdHere5.text = "You have ${amountOfCoins} ${coinSymbol}\n${amountOfCoins} x ${correctPriceFormat}\nValue ${value} USD"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentCurrencyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageString: String? = intent?.getStringExtra("imageString")
        val coinId: String? = intent?.getStringExtra("coinId")
        val coinSymbol: String? = intent?.getStringExtra("coinSymbol")
        val coinName: String? = intent?.getStringExtra("coinName")
        val coinPrice: String? = intent?.getStringExtra("coinPrice")
        val amountOfCoins: Float = intent.getFloatExtra("amountOfCoins",0F)

        if(imageString == null || coinName == null || coinSymbol == null || coinPrice == null || coinId == null){
            showError()
        } else {
            test(imageString,coinSymbol,coinName,coinPrice, coinId)

            /*binding.button3.setOnClickListener{
                test(imageString,coinSymbol,coinName,coinPrice, coinId)
            }*/

            binding.button.setOnClickListener{
                supportFragmentManager.beginTransaction().apply{
                    replace(R.id.purchase_fragment_container, BuyCurrencyFragment.newInstance(imageString,coinName,coinSymbol,coinPrice, amountOfCoins))
                        .addToBackStack("Currency")
                        .commit()
                }
            }

            binding.button2.setOnClickListener{
                supportFragmentManager.beginTransaction().apply{
                    replace(R.id.purchase_fragment_container, SellCurrencyFragment.newInstance(imageString,coinName,coinSymbol,coinPrice, amountOfCoins, coinId)) // Trenger ikke coinName for sell
                        .addToBackStack("Currency")
                        .commit()
                }
            }
            supportFragmentManager.addOnBackStackChangedListener {
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

            val balanceText = findViewById<View>(R.id.user_balance) as TextView
            balanceText.text = "Balance: ${balance.toString()}$"
        }
    }
    fun setTransactionList(list: List<Transactions>) {
        transactionList.clear()
        transactionList.addAll(list)
    }

}
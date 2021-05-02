package com.example.cryptocurrency

import android.app.PendingIntent.getActivity
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.cryptocurrency.databinding.FragmentCurrencyBinding
import com.example.cryptocurrency.details.BuyCurrencyFragment
import com.example.cryptocurrency.details.SellCurrencyFragment
import com.example.cryptocurrency.details.TransactionsViewModel
import com.example.cryptocurrency.list.CurrencyFragment
import com.example.cryptocurrency.list.TransactionsListViewModel

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

    val viewModel: TransactionsListViewModel by viewModels()

    fun test(){
        val coinName: String? = intent?.getStringExtra("coinName")
        if(coinName != null){
            viewModel.fetchAllData()
            viewModel.fetchAmountOfCoinsByName(coinName)

            val test: Float? = viewModel.sumAmountOfCoinsByNameLiveData.value
            if(test != null){
                binding.someTextIdHere5.text = "TESTING ${test}"
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentCurrencyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageString: String? = intent?.getStringExtra("imageString")
        val coinSymbol: String? = intent?.getStringExtra("coinSymbol")
        val coinName: String? = intent?.getStringExtra("coinName")
        val coinPrice: String? = intent?.getStringExtra("coinPrice")
        //val changePercent24h: String? = intent?.getStringExtra("changePercent24h")
        //val amountOfCoins: String? = intent?.getStringExtra("amountOfCoins")
        val amountOfCoins: Float = intent.getFloatExtra("amountOfCoins",0F)

        val correctPriceFormat: String = "$" + coinPrice?.substring(0, coinPrice.indexOf(".") + 3)

        if(imageString == null || coinName == null || coinSymbol == null || coinPrice == null ){
            showError()
        } else {
            Glide.with(this).load(imageString).into(
                    binding.someImgNameHere
            )
            binding.someTextIdHere.text = coinName
            binding.someTextIdHere2.text = coinSymbol
            binding.someTextIdHere3.text = correctPriceFormat
            val value: Float = amountOfCoins * coinPrice.toFloat()
            binding.someTextIdHere5.text = "You have ${amountOfCoins} ${coinSymbol}\n${amountOfCoins} x ${correctPriceFormat}\nValue ${value} USD"
        }

        binding.button3.setOnClickListener{
            test()
        }

        binding.button.setOnClickListener{
            supportFragmentManager.beginTransaction().apply{
                replace(R.id.purchase_fragment_container, BuyCurrencyFragment.newInstance(imageString,coinName,coinSymbol,coinPrice, amountOfCoins))
                        .addToBackStack("Currency")
                        .commit()
            }
        }

        binding.button2.setOnClickListener{
            supportFragmentManager.beginTransaction().apply{
                replace(R.id.purchase_fragment_container, SellCurrencyFragment.newInstance(imageString,coinName,coinSymbol,coinPrice, amountOfCoins))
                    .addToBackStack("Currency")
                    .commit()
            }
        }


    }

    private fun showError() {
        Log.d("ERROR", "Errorrrr")
    }

}
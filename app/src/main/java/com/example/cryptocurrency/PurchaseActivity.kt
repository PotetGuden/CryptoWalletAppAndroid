package com.example.cryptocurrency

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.cryptocurrency.databinding.ActivityPurchaseBinding
import com.example.cryptocurrency.fragments.BuySellFragment
import com.example.cryptocurrency.viewModels.TransactionsListViewModel
import com.example.cryptocurrency.viewModels.ApiViewModel
import java.math.BigDecimal
import java.math.RoundingMode

class PurchaseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPurchaseBinding

    private val viewModel: TransactionsListViewModel by viewModels()
    private val currencyListViewModel: ApiViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPurchaseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val imageString: String? = intent?.getStringExtra("imageString")
        val coinId: String? = intent?.getStringExtra("coinId")
        val coinSymbol: String? = intent?.getStringExtra("coinSymbol")
        val coinName: String? = intent?.getStringExtra("coinName")
        val coinPrice: String? = intent?.getStringExtra("coinPrice")


        if(imageString == null || coinName == null || coinSymbol == null || coinPrice == null || coinId == null){
            showError()
        } else {
            supportFragmentManager.beginTransaction()
                .replace(R.id.purchase_fragment_container, BuySellFragment.newInstance(coinName, coinSymbol, coinPrice, coinId))
                .commit()

            Glide.with(this).load(imageString).into(
                binding.imageId
            )
            binding.coinName.text = coinName
            binding.coinSymbol.text = coinSymbol

            currencyListViewModel.allCurrencies.observe(this){
                for(i in it.data){
                    if(i.id == coinId){
                        val priceFormatted = BigDecimal(i.priceUsd.toDouble()).setScale(
                            2,
                            RoundingMode.HALF_EVEN
                        )
                        binding.updatedPrice.text = "$${priceFormatted}"
                    }
                }
            }
            supportFragmentManager.addOnBackStackChangedListener{
                currencyListViewModel.LoadCoinFromList()
            }
        }
    }

    private fun showError() {
        Log.d("ERROR", "Some error message")
    }

    override fun onResume() {
        super.onResume()
        currencyListViewModel.LoadCoinFromList()
    }
}
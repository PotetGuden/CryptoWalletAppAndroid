package com.example.cryptocurrency

import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.text.TextUtils.replace
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.cryptocurrency.databinding.ActivityPurchaseBinding
import com.example.cryptocurrency.databinding.FragmentCurrencyBinding
import com.example.cryptocurrency.details.BuyCurrencyFragment
import com.example.cryptocurrency.details.BuySellFragment
import com.example.cryptocurrency.details.SellCurrencyFragment
import com.example.cryptocurrency.details.TransactionsViewModel
import com.example.cryptocurrency.entities.Transactions
import com.example.cryptocurrency.list.CurrencyFragment
import com.example.cryptocurrency.list.TransactionsListViewModel
import java.util.*

class PurchaseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPurchaseBinding

    private val viewModel: TransactionsListViewModel by viewModels()
    private val currencyListViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPurchaseBinding.inflate(layoutInflater)
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
            supportFragmentManager.beginTransaction()
                .replace(R.id.purchase_fragment_container, BuySellFragment.newInstance(imageString,coinName,coinSymbol,coinPrice,amountOfCoins, coinId))
                .commit()
        }
    }

    private fun showError() {
        Log.d("ERROR", "Errorrrr")
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        Log.d("Purchase Activity", "onUserInteraction")
        currencyListViewModel.LoadCoinFromList()
    }

    override fun onResume() {
        super.onResume()
        Log.d("Purchase Activity", "onResume")
    }
}
package com.example.cryptocurrency

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.cryptocurrency.databinding.ActivityMainBinding
import com.example.cryptocurrency.details.PortofolioFragment
import com.example.cryptocurrency.details.TransactionsViewModel
import com.example.cryptocurrency.list.CurrencyListFragment
import com.example.cryptocurrency.list.TransactionsListViewModel
import java.math.BigDecimal
import java.math.RoundingMode


// NOTE: Fordi man "kapper" floaten sånn at man bare får x antall siffer etter 0, så vil det egt bli litt feilmargin i balance/transaksjon (not)

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreference: SharedPreferences

    val apiViewModel: MainViewModel by viewModels()
    val viewModel: TransactionsListViewModel by viewModels()
    val transactionViewModel: TransactionsViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        //setContentView(R.layout.activity_main)
        setContentView(binding.root)
        supportActionBar?.hide()

        //binding.userBalance.text = newString

        supportFragmentManager.beginTransaction()
            .replace(
                R.id.currency_fragment_container,
                CurrencyListFragment(), "yolo"
            )
            .commit()

        viewModel.init(this)
        viewModel.fetchAllData()
        //updateBalance()

        sharedPreference = getSharedPreferences("init-transaction", Context.MODE_PRIVATE)
        val keyValueData = sharedPreference
            .getString("key_data", "null")
        if(keyValueData == "null"){
            Toast.makeText(this, keyValueData, Toast.LENGTH_SHORT).show()
            val editor = sharedPreference.edit()
            editor.putString("key_data", "Well everything is fine..")
            editor.putBoolean("KEY_TRUE_FALSE_CHECK", true)
            editor.putFloat("balanceUSD", 10000F)
            editor.apply() // commit()
            transactionViewModel.init(this)
            transactionViewModel.save("Dollar","usd", -10000F, 1F)
            viewModel.fetchAllData()
            updateBalance()
            Toast.makeText(this, "You got $10.000 as a Installation Reward!", Toast.LENGTH_SHORT).show()
        }

        binding.headerTitle.setOnClickListener{
            supportFragmentManager.beginTransaction()
                .add(
                    R.id.currency_fragment_container,
                    PortofolioFragment(), "xdd"
                )    // Sende med noe til portofolio?
                    .addToBackStack("Portofolio")
                    .commit()
        }

        binding.test.setOnClickListener{
            val keyValueData = sharedPreference
                .getString("key_data", "null")
            if(keyValueData == "null"){
                Toast.makeText(this, keyValueData, Toast.LENGTH_SHORT).show()
                val editor = sharedPreference.edit()
                editor.putString("key_data", "Well everything is fine..")
                editor.putBoolean("KEY_TRUE_FALSE_CHECK", true)
                editor.putFloat("balanceUSD", 10000F)
                editor.apply() // commit()
                transactionViewModel.init(this)
                transactionViewModel.save("Dollar","usd", -10000F, 1F)
                viewModel.fetchAllData()
                updateBalance()
            } else{
                Toast.makeText(this, "first time login", Toast.LENGTH_SHORT).show()
            }
        }

        val deleteBtn = findViewById<View>(R.id.delete_id)
        deleteBtn.setOnClickListener{
            viewModel.deleteData()
            // Delete
            sharedPreference.edit().remove("key_data").apply()
            sharedPreference.edit().clear().apply()
        }

        val updateBtn = findViewById<View>(R.id.update_id)
        updateBtn.setOnClickListener{
            // BARE EN TEST
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.currency_fragment_container,
                    CurrencyListFragment(), "yolo"
                )
                .commit()
            updateBalance()
        }
    }

    override fun onResume() {
        super.onResume()
        updateBalance()
        Log.d("Updated Coin Value", "ON RESUME MAIN ACTIVITY")
    }

    //private val transactionList = mutableListOf<Transactions>()
    //private val updatedCurrencyList = mutableListOf<Coins>()
    // TODO FJERN ALLE INDICES MED FOREACH
    fun updateBalance(){
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

    override fun onUserInteraction() {
        super.onUserInteraction()
        updateBalance()
    }
    /*fun setTransactionList(list: List<Transactions>) {
        transactionList.clear()
        transactionList.addAll(list)
    }
    fun setUpdatedCurrencyList(list: List<Coins>) {
        updatedCurrencyList.clear()
        updatedCurrencyList.addAll(list)

    }*/

/*
*  viewModel.transactionListLiveData.observe(viewLifecycleOwner){
            adapter.setTransactionList(it)

            for(i in it){
                balance += i.updatedPrice * i.amountOfCoin
            }
            val balanceText = requireActivity().findViewById<View>(R.id.user_balance) as TextView
            balanceText.text = "Balance: ${balance}$"
        }
*
* */

    //supportActionBar?.hide()
    /*Handler().postDelayed({
        val intent = Intent(this@MainActivity, HomeActivity::class.java)
        startActivity(intent) // Sender oss til neste skjerm
        finish()    // Gjør at vi ikke kan trykke tilbake knappen for å komme til splash screen igjen
    }, 1000)*/
}

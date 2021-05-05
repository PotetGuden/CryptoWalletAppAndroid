package com.example.cryptocurrency

import android.content.Context
import android.content.SharedPreferences
import android.icu.util.TimeZone
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.example.cryptocurrency.databinding.ActivityMainBinding
import com.example.cryptocurrency.details.PortofolioFragment
import com.example.cryptocurrency.details.TransactionsViewModel
import com.example.cryptocurrency.list.CurrencyListFragment
import com.example.cryptocurrency.list.TransactionsListViewModel
import kotlinx.coroutines.joinAll
import java.time.*
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.Locale.setDefault


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
        updateBalance()

        sharedPreference = getSharedPreferences("init-transaction", Context.MODE_PRIVATE)

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
            updateBalance()
            // BARE EN TEST
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.currency_fragment_container,
                    CurrencyListFragment(), "yolo"
                )
                .commit()
        }
    }

    override fun onResume() {
        super.onResume()
        updateBalance()
    }
    //private val transactionList = mutableListOf<Transactions>()

    fun updateBalance(){

        viewModel.fetchAllData()

        Log.d("Updated Coin Value", "${viewModel.transactionListLiveData.value}")
        viewModel.transactionListLiveData.observe(this){
            var balance = 0F
            //setTransactionList(it)
            if(it.isEmpty()){
                Log.d("Database", "is empty!")
                // Kanskje legge til installation reward her?
            } else{
                for (i in it.indices) {
                    if(i == 0){
                        //balance += 10000F
                        continue
                    }
                    apiViewModel.LoadCoinByName(it[i].coinId)
                    var updatedPrice = 0F
                    apiViewModel.specificCoin.observe(this){    updatedCurrency ->
                        if(it[i].coinId == updatedCurrency.data.id){
                            Log.d("Updated Coin Value", "i:${i} = Coin ID: ${it[i].coinId} Updated Price = ${updatedCurrency.data.priceUsd}")
                            //balance += it[i].amountOfCoin * updatedCurrency.data.priceUsd.toFloat() *-1F
                            updatedPrice = updatedCurrency.data.priceUsd.toFloat()
                        }
                    }
                    balance += it[i].amountOfCoin * updatedPrice *-1F
                    //balance += it[i].updatedPrice*it[i].amountOfCoin *-1F // FUNKER ISH
                }
                for(i in it.indices){   // CURRENCY BALANCE
                    if(i == 0){  // Dropping installation reward
                        continue
                    }
                    apiViewModel.LoadCoinByName(it[i].coinId)
                    apiViewModel.specificCoin.observe(this){    updatedCurrency ->
                        if(it[i].coinId == updatedCurrency.data.id){
                            balance += it[i].amountOfCoin * updatedCurrency.data.priceUsd.toFloat() *-1F
                        }
                    }
                    //balance += it[i].updatedPrice*it[i].amountOfCoin  // FUNKER ISH
                }
            }
            binding.userBalance.text = "Balance: ${balance}$ FIKS BALANCE"
            //val balanceText = findViewById<View>(R.id.user_balance) as TextView
            //balanceText.text = "Balance: ${balance.toString()}$"
        }

    }

    /*fun setTransactionList(list: List<Transactions>) {
        transactionList.clear()
        transactionList.addAll(list)
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

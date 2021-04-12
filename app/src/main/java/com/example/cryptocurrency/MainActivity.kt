package com.example.cryptocurrency

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.cryptocurrency.databinding.ActivityMainBinding
import com.example.cryptocurrency.details.PortofolioFragment
import com.example.cryptocurrency.list.CurrencyFragment
import com.example.cryptocurrency.list.CurrencyListFragment
import com.example.cryptocurrency.list.TransactionsListViewModel
import com.google.android.material.snackbar.Snackbar

// NOTE: Fordi man "kapper" floaten sånn at man bare får x antall siffer etter 0, så vil det egt bli litt feilmargin i balance/transaksjon

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    val viewModel: TransactionsListViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        //setContentView(R.layout.activity_main)
        setContentView(binding.root)
        supportActionBar?.hide()

        //binding.userBalance.text = newString

        supportFragmentManager.beginTransaction()
            .replace(R.id.currency_fragment_container,
                CurrencyListFragment(),"yolo")
            .commit()

        viewModel.init(this)
        updateBalance()


        binding.headerTitle.setOnClickListener{
            supportFragmentManager.beginTransaction()
                .replace(R.id.currency_fragment_container,
                    PortofolioFragment(), "xdd")    // Sende med noe til portofolio?
                    .addToBackStack("Portofolio")
                    .commit()
        }
        /*fun updateBalance(){
            viewModel.transactionListLiveData.observe(this){
                if(it.isEmpty()){
                    Log.d("Database", "is empty!")
                } else{
                    for (i in it.indices) {
                        balance += it[i].updatedPrice*it[i].amountOfCoin
                    }
                }

                val balanceText = findViewById<View>(R.id.user_balance) as TextView
                balanceText.text = "Balance: ${balance.toString()}$"
            }
        }*/


        val deleteBtn = findViewById<View>(R.id.delete_id)
        deleteBtn.setOnClickListener{
            viewModel.deleteData()
        }

        val updateBtn = findViewById<View>(R.id.update_id)
        updateBtn.setOnClickListener{
            updateBalance()
        }

        //supportActionBar?.hide()
        /*Handler().postDelayed({
            val intent = Intent(this@MainActivity, HomeActivity::class.java)
            startActivity(intent) // Sender oss til neste skjerm
            finish()    // Gjør at vi ikke kan trykke tilbake knappen for å komme til splash screen igjen
        }, 1000)*/

    }
    fun updateBalance(){  // sikkert minneproblemer med balance : Float?
        var balance : Float = 0.0F
        viewModel.transactionListLiveData.observe(this){
            if(it.isEmpty()){
                Log.d("Database", "is empty!")
                // Kanskje legge til installation reward her?
            } else{
                for (i in it.indices) {
                    balance += it[i].updatedPrice*it[i].amountOfCoin
                }
            }

            val balanceText = findViewById<View>(R.id.user_balance) as TextView
            balanceText.text = "Balance: ${balance.toString()}$"
        }
    }
    /*fun String LoadImageFromUrl(url: String){
        val URL = "https://static.coincap.io/assets/icons/"+url+"@2x.png"
        return URL
    }
     */
}

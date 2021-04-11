package com.example.cryptocurrency

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.cryptocurrency.databinding.ActivityMainBinding
import com.example.cryptocurrency.list.CurrencyFragment
import com.example.cryptocurrency.list.CurrencyListFragment
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        //setContentView(R.layout.activity_main)
        setContentView(binding.root)
        supportActionBar?.hide()

        supportFragmentManager.beginTransaction()
            .replace(R.id.currency_fragment_container,
                CurrencyListFragment(),"yolo")
            .commit()

        viewModel.allCurrencies.observe(this){ currencies->
            binding.tmpId.text = currencies.data.lastIndex.toString()
            Glide.with(this).load("https://static.coincap.io/assets/icons/${currencies.data[1].symbol.toLowerCase()}@2x.png").into(
                binding.image3
            )
        }

        viewModel.bitcoin.observe(this){ coin->
            binding.directlySpecificCoinTmpId.text = coin.data.id
            Glide.with(this).load("https://static.coincap.io/assets/icons/" + coin.data.symbol.toLowerCase() + "@2x.png").into(
                binding.image2
            )
            val correctPriceFormat: String = "$" + coin.data.priceUsd.substring(0,coin.data.priceUsd.indexOf(".")+3)
            binding.directlySpecificCoinTmpIdValue.text = correctPriceFormat
        }

        viewModel.specificCoin.observe(this){ specificCurrency->
            binding.usersChoiceCoinTmpId.text = specificCurrency.data.id
            Glide.with(this).load("https://static.coincap.io/assets/icons/" + specificCurrency.data.symbol.toLowerCase() + "@2x.png").into(
                binding.image
            )

        }

        viewModel.error.observe(this) { error ->
            if(error){
                Snackbar.make(binding.root, "En feil skjedde, har du internet?", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Last på nytt", { viewModel.LoadBitcoin() }).show()
            }
        }
        //supportActionBar?.hide()
        /*Handler().postDelayed({
            val intent = Intent(this@MainActivity, HomeActivity::class.java)
            startActivity(intent) // Sender oss til neste skjerm
            finish()    // Gjør at vi ikke kan trykke tilbake knappen for å komme til splash screen igjen
        }, 1000)*/

    }

    /*fun String LoadImageFromUrl(url: String){
        val URL = "https://static.coincap.io/assets/icons/"+url+"@2x.png"
        return URL
    }
     */
}
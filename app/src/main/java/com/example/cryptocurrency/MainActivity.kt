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

        //binding.userBalance.text = newString

        supportFragmentManager.beginTransaction()
            .replace(R.id.currency_fragment_container,
                CurrencyListFragment(),"yolo")
            .commit()


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
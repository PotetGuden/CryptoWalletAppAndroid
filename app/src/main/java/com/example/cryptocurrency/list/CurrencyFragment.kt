package com.example.cryptocurrency.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.LiveData
import com.bumptech.glide.Glide
import com.example.cryptocurrency.Coins
import com.example.cryptocurrency.R
import com.example.cryptocurrency.databinding.FragmentCoinInfoBinding
import com.example.cryptocurrency.databinding.FragmentCurrencyBinding
import com.example.cryptocurrency.details.BuyCurrencyFragment
import com.example.cryptocurrency.entities.Transactions


class CurrencyFragment : Fragment(R.layout.fragment_currency) { // fragment_currency

    private lateinit var binding: FragmentCurrencyBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCurrencyBinding.bind(view)

        /*val item : Int? = arguments?.getInt("item") // må ha ifsjekk fordi arguments kan være null ellers funker ikke setImageResource(item)
        if(item == null){
            showError()
        } else{
            Glide.with(this).load("https://static.coincap.io/assets/icons/btc@2x.png").into(
                    binding.someImgNameHere // item
            )
            binding.someTextIdHere.text = bitcoin
            binding.someTextIdHere.text = when(item){
                R.drawable.chicken -> getString(R.string.chicken)
                R.drawable.sheep -> getString(R.string.sheep)
                R.drawable.cow -> getString(R.string.cow)
                else -> ""
            }
        }*/

        val imgName : String? = arguments?.getString("imgName")
        val coinName : String? = arguments?.getString("coinName")
        val coinSymbol : String? = arguments?.getString("coinSymbol")
        val coinPrice : String? = arguments?.getString("coinPrice")
        val changePercent24h : String? = arguments?.getString("changePercent24h")
        val correctPriceFormat: String = "$" + coinPrice?.substring(0,coinPrice.indexOf(".")+3)
        val correctPercentChangeFormat: String = changePercent24h?.substring(0,changePercent24h.indexOf(".")+3) + "%"

        val amountOfCoin : Float? = arguments?.getFloat("amountOfCoins")

        if(imgName == null || coinName == null || coinSymbol == null || coinPrice == null || changePercent24h == null ){
            showError()
        } else{
            Glide.with(this).load(imgName).into(
                    binding.someImgNameHere
            )
            binding.someTextIdHere.text = coinName
            binding.someTextIdHere2.text = coinSymbol
            binding.someTextIdHere3.text = correctPriceFormat
            //binding.someTextIdHere4.text = correctPercentChangeFormat
            val value: Float = amountOfCoin!! * coinPrice.toFloat()
            binding.someTextIdHere5.text = "You have ${amountOfCoin.toString()} ${coinSymbol}\n${amountOfCoin.toString()} x ${correctPriceFormat}\nValue ${value} USD"
            // You have ${amountOfCoin} aoisjdsaoi
        }

        binding.button.setOnClickListener{
            fragmentManager?.beginTransaction()?.apply{
                replace(R.id.currency_fragment_container, BuyCurrencyFragment.newInstance(imgName,coinName,coinSymbol,coinPrice, 12F))
                    .addToBackStack("Currency")
                    .commit()
            }
        }
    }

    private fun showError() {
        TODO("Not yet implemented")
    }

    /*companion object { // static function - har tilang til arguments som man sender til newInstance()
        fun newInstance(item: Int): CurrencyFragment = CurrencyFragment().apply{
            arguments = Bundle().apply{
                putInt("item", item)
            }
        }
    }*/
    companion object { // static function - har tilang til arguments som man sender til newInstance()
        fun newInstance(coin: Coins, amountOfCoins: Float): CurrencyFragment = CurrencyFragment().apply{
            arguments = Bundle().apply{
                val imageString = "https://static.coincap.io/assets/icons/${coin.symbol.toLowerCase()}@2x.png"

                putString("imgName", imageString)
                putString("coinSymbol", coin.symbol)
                putString("coinName", coin.name)
                putString("coinPrice", coin.priceUsd)
                putString("changePercent24h", coin.changePercent24Hr)

                //putString("amountOfCoins", amountOfCoins.toString())
                putFloat("amountOfCoins", amountOfCoins)
            }
        }
    }



}



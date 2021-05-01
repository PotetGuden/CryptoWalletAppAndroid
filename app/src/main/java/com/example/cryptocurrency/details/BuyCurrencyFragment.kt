package com.example.cryptocurrency.details

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asFlow
import androidx.room.Transaction
import com.bumptech.glide.Glide
import com.example.cryptocurrency.R
import com.example.cryptocurrency.databinding.FragmentBuyCurrencyBinding
import com.example.cryptocurrency.entities.Transactions
import com.example.cryptocurrency.list.CurrencyListFragment
import com.example.cryptocurrency.list.TransactionsListFragment
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.NumberFormat

class BuyCurrencyFragment : Fragment(R.layout.fragment_buy_currency){

    private var transactionsID: Long? = null
    private lateinit var binding: FragmentBuyCurrencyBinding
    private val  viewModel: TransactionsViewModel by lazy(){
        ViewModelProvider(this).get(TransactionsViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBuyCurrencyBinding.bind(view)
        transactionsID = arguments?.getLong("transactionsID")

        viewModel.init(requireContext())

        val imgName : String? = arguments?.getString("imgName")
        val coinName : String? = arguments?.getString("coinName")
        val coinSymbol : String? = arguments?.getString("coinSymbol")
        val coinPrice : String? = arguments?.getString("coinPrice")
        val amountOfCoins : Float = arguments!!.getFloat("amountOfCoins")
        val correctPriceFormat: String = "$" + coinPrice?.substring(0,coinPrice.indexOf(".")+3)

        if(imgName == null || coinName == null || coinSymbol == null || coinPrice == null ){

        } else{

            initViewListeners(coinName, coinPrice)
            binding.button.text = "BUY"
            binding.coinSymbol.text = coinSymbol
            binding.balanceMessage.text = "You can only buy cryptocurrency in USD\n\nYou have 'BALANCE' USD"
            //binding.someTextIdHere4.text = correctPercentChangeFormat*/
        }

        binding.editText.addTextChangedListener(object : TextWatcher{

            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s.toString().toFloatOrNull() != null){
                    var currencyAmount: Double = s.toString().toDouble() / coinPrice!!.toDouble()
                    //BigDecimal(currencyAmount).setScale(2, RoundingMode.HALF_EVEN)
                    binding.editText2.setText(currencyAmount.toString())
                } else{
                    binding.editText2.setText("")
                }
            }
        })
    }

    companion object { // static function - har tilgang til arguments som man sender til newInstance()
        fun newInstance(imgName: String?, coinName: String?, coinSymbol: String?, coinPrice: String?, amountOfCoins: Float): BuyCurrencyFragment = BuyCurrencyFragment().apply{
            arguments = Bundle().apply{
                //val imageString = "https://static.coincap.io/assets/icons/${coin.symbol.toLowerCase()}@2x.png"

                putString("imgName", imgName)
                putString("coinSymbol", coinName)
                putString("coinName", coinSymbol)
                putString("coinPrice", coinPrice)
                putFloat("amountOfCoins", amountOfCoins)
            }
        }
    }

    private fun initViewListeners(coinName: String, coinPrice: String){
        with(binding){
            button.setOnClickListener{
                val amountOfCoins = editText.text.toString().toFloat()
                viewModel.save(coinName,coinPrice.toFloat(),amountOfCoins)

                viewModel.transactionLiveData.observe(viewLifecycleOwner){
                    var balance : Float = it.amountOfCoin*it.updatedPrice
                    val balanceText = activity!!.findViewById<View>(R.id.user_balance) as TextView
                    balanceText.text = balance.toString()
                }

                fragmentManager?.popBackStack()
                /*val balanceText = activity!!.findViewById<View>(R.id.user_balance) as TextView
                balanceText.text = "NEW BALANCE"*/
            }


        }
    }

}
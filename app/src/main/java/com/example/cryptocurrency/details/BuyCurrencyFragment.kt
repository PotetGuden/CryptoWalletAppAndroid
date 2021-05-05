package com.example.cryptocurrency.details

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.cryptocurrency.R
import com.example.cryptocurrency.databinding.FragmentBuyCurrencyBinding
import com.example.cryptocurrency.list.TransactionsListViewModel
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

class BuyCurrencyFragment : Fragment(R.layout.fragment_buy_currency){

    private var transactionsID: Long? = null
    private lateinit var binding: FragmentBuyCurrencyBinding
    private val  viewModel: TransactionsViewModel by lazy(){
        ViewModelProvider(this).get(TransactionsViewModel::class.java)
    }
    private val transactionListViewModel: TransactionsListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBuyCurrencyBinding.bind(view)
        transactionsID = arguments?.getLong("transactionsID")

        viewModel.init(requireContext())

        val imgName : String? = arguments?.getString("imgName")
        val coinId : String? = arguments?.getString("coinId")
        val coinName : String? = arguments?.getString("coinName")
        val coinSymbol : String? = arguments?.getString("coinSymbol")
        val coinPrice : String? = arguments?.getString("coinPrice")
        val amountOfCoins : Float = requireArguments().getFloat("amountOfCoins")
        val correctPriceFormat: String = "$" + coinPrice?.substring(0, coinPrice.indexOf(".") + 3)

        if(imgName == null || coinName == null || coinSymbol == null || coinPrice == null || coinId == null ){

        } else{
            initViewListeners(coinId, coinSymbol, coinPrice)
            binding.button.text = "BUY"
            binding.coinSymbol.text = coinName

            transactionListViewModel.init(requireContext())
            transactionListViewModel.fetchSumBalance()

            var balanceUSD = 0F
            transactionListViewModel.sumBalance.observe(viewLifecycleOwner){ balance ->
                balanceUSD += balance*-1F       // Convert negative value to positive, because of database structure
                binding.balanceMessage.text = "You can only buy cryptocurrency in USD\n\nYou have ${balanceUSD} USD"
            }

            val df = DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH))
            df.maximumFractionDigits = 4

            binding.editText.addTextChangedListener(object :
                TextWatcher { // bytt til it aka fjern object
                override fun afterTextChanged(s: Editable?) {}
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s.toString() != "") {
                        binding.button.isEnabled = s.toString().toFloat() <= balanceUSD

                        val currencyAmount: Float = s.toString().toFloat() / coinPrice.toFloat()

                        binding.editText2.text = df.format(currencyAmount)
                    } else {
                        binding.editText2.text = ""
                    }
                }
            })
        }
    }

    companion object { // static function - har tilgang til arguments som man sender til newInstance()
        fun newInstance(
            imgName: String?,
            coinId: String?,
            coinName: String?,
            coinSymbol: String?,
            coinPrice: String?,
            amountOfCoins: Float
        ): BuyCurrencyFragment = BuyCurrencyFragment().apply{
            arguments = Bundle().apply{
                //val imageString = "https://static.coincap.io/assets/icons/${coin.symbol.toLowerCase()}@2x.png"

                putString("imgName", imgName)
                putString("coinSymbol", coinSymbol)
                putString("coinName", coinName)
                putString("coinId", coinId)
                putString("coinPrice", coinPrice)
                putFloat("amountOfCoins", amountOfCoins)
            }
        }
    }

    private fun initViewListeners(coinId: String,coinName: String, coinPrice: String){
        with(binding){
            button.setOnClickListener{
                //val amountOfUSD = editText.text.toString().toFloat()
                val amountOfCoins = editText2.text.toString().toFloat()
                viewModel.save(coinId, coinName, coinPrice.toFloat(), amountOfCoins)
                parentFragmentManager.popBackStack()
                /*viewModel.transactionLiveData.observe(viewLifecycleOwner){
                    var balance : Float = it.amountOfCoin*it.updatedPrice
                    val balanceText = activity!!.findViewById<View>(R.id.user_balance) as TextView
                    balanceText.text = balance.toString()
                }

                fragmentManager?.popBackStack()*/
                /*val balanceText = activity!!.findViewById<View>(R.id.user_balance) as TextView
                balanceText.text = "NEW BALANCE"*/
            }


        }
    }

}
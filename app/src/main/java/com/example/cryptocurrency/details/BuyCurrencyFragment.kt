package com.example.cryptocurrency.details

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
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
    private val viewModel: TransactionsViewModel by lazy(){
        ViewModelProvider(this).get(TransactionsViewModel::class.java)
    }
    private val transactionListViewModel: TransactionsListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBuyCurrencyBinding.bind(view)
        transactionsID = arguments?.getLong("transactionsID")

        viewModel.init(requireContext())

        val coinId : String? = arguments?.getString("coinId")
        val coinName : String? = arguments?.getString("coinName")
        val coinSymbol : String? = arguments?.getString("coinSymbol")
        val coinPrice : String? = arguments?.getString("coinPrice")

        if(coinName == null || coinSymbol == null || coinPrice == null || coinId == null ){
            // Show some error if wanted
        } else{
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

            binding.editText.addTextChangedListener{
                if (it.toString() != "") {
                    // Uncomment below to disable buy button if amount entered is higher than your usd balance (instead of making the toast)
                    // binding.button.isEnabled = s.toString().toFloat() <= balanceUSD
                    val currencyAmount: Float = it.toString().toFloat() / coinPrice.toFloat()
                    binding.editText2.text = df.format(currencyAmount)
                } else {
                    binding.editText2.text = ""  // Remove existing values
                }
            }

            binding.button.setOnClickListener{
                val currencyAmount: Float = binding.editText.text.toString().toFloat() / coinPrice.toFloat()
                val purchaseAmount = binding.editText.text.toString().toFloat()
                if(purchaseAmount > balanceUSD){
                    parentFragmentManager.popBackStack()
                    Toast.makeText(this.context, "You dont have enough money", Toast.LENGTH_LONG).show()
                } else{
                    viewModel.save(coinId, coinSymbol, coinPrice.toFloat(), currencyAmount)
                    parentFragmentManager.popBackStack()
                }
            }
        }
    }

    companion object {
        fun newInstance(coinId: String?, coinName: String?, coinSymbol: String?, coinPrice: String?): BuyCurrencyFragment = BuyCurrencyFragment().apply{
            arguments = Bundle().apply{
                putString("coinSymbol", coinSymbol)
                putString("coinName", coinName)
                putString("coinId", coinId)
                putString("coinPrice", coinPrice)
            }
        }
    }
}
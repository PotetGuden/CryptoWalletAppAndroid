package com.example.cryptocurrency.details

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.cryptocurrency.R
import com.example.cryptocurrency.databinding.FragmentTransactionBinding
import com.example.cryptocurrency.list.TransactionsListViewModel

class TransactionFragment : Fragment(R.layout.fragment_transaction) {

    private var transactionID: Long? = null
    private lateinit var binding: FragmentTransactionBinding
    private val viewModel: TransactionsViewModel by lazy(){
        ViewModelProvider(this).get(TransactionsViewModel::class.java)
    }

    companion object {
        fun newInstance(transactionID: Long? = null) = TransactionFragment().apply {
            if (transactionID != null) {
                arguments = Bundle().apply {
                    putLong("transactionID", transactionID)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Init
        binding = FragmentTransactionBinding.bind(view)
        transactionID = arguments?.getLong("transactionID")
        // Init view model
        viewModel.init(requireContext()) // .studentID
        // View listeners
        initViewListeners()

        viewModel.transactionLiveData.observe(viewLifecycleOwner) {
            with(binding) {
                val amount : String = it.amountOfCoin.toString()
                val coinName : String = it.coinName
                val updatedPrice : String = it.updatedPrice.toString()
                val transactionInformation = "${amount} ${coinName} for ${updatedPrice} USD"
                //binding.someTextIdHere2.text = transactionInformation
                someTextIdHere.text = it.coinName
                someTextIdHere2.text = it.amountOfCoin.toString()
                someTextIdHere3.text = it.updatedPrice.toString()
                //saveButton.setText(R.string.update)
            }
        }
    }

    private fun initViewListeners() {
        /*with(binding) {
           button.setOnClickListener {
                if (studentID == null) {
                    viewModel.saveData(name.text.toString(), course.text.toString())
                } else {
                    viewModel.updateData(studentID!!, name.text.toString(), course.text.toString())
                }
            }
        }*/
        /*viewModel.transactionLiveData.observe(viewLifecycleOwner){
                if (transactionID == null) {
                    Log.d("Ny Ting", "Noe nytt skjedde.")
                    viewModel.save(it.coinName, it.updatedPrice, it.amountOfCoin)
                } else {
                    viewModel.update(transactionID!!, it.coinName, it.updatedPrice, it.amountOfCoin)
                }
        }*/


    }
}
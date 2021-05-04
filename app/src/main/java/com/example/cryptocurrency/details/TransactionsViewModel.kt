package com.example.cryptocurrency.details

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptocurrency.database.DataBase
import com.example.cryptocurrency.database.TransactionsDAO
import com.example.cryptocurrency.entities.Transactions
import kotlinx.coroutines.launch


class TransactionsViewModel : ViewModel() {

    private lateinit var transactionDao: TransactionsDAO

    private val _transactionLiveData: MutableLiveData<Transactions> = MutableLiveData()
    val transactionLiveData: LiveData<Transactions> = _transactionLiveData

    fun init(context: Context){
        transactionDao = DataBase.getDatabase(context).getTransactionDAO()
    }

    fun save(coinName: String, updatedPrice: Float, amountOfCoin: Float){
        viewModelScope.launch{
            transactionDao.insert(Transactions(coinName = coinName, updatedPrice = updatedPrice, amountOfCoin = amountOfCoin))
            Log.d("Save", "insert happend.")
        }
    }

    fun update(transactionsId: Long, coinName: String?, updatedPrice: Float, amountOfCoin: Float){
        viewModelScope.launch{
            if(coinName.isNullOrEmpty()){ // updatedPrice.isNullOrEmpty? isNan?
                return@launch // Kan vel legge inn en melding til user her?
            }
            transactionDao.update(Transactions(transactionsId = transactionsId, coinName = coinName, updatedPrice = updatedPrice, amountOfCoin = amountOfCoin))
        }
    }
}
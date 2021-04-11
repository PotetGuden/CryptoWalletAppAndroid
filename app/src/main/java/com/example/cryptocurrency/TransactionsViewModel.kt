package com.example.cryptocurrency

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptocurrency.database.DataBase
import com.example.cryptocurrency.database.TransactionsDAO
import com.example.cryptocurrency.entities.Transactions
import kotlinx.coroutines.launch


class TransactionsViewModel : ViewModel() {

    private lateinit var transactionDao: TransactionsDAO

    fun init(context: Context){
        transactionDao = DataBase.getDatabase(context).getTransactionDAO()
    }

    fun save(coinName: String?, updatedPrice: Float, amountOfCoin: Float){
        viewModelScope.launch{
            if(coinName.isNullOrEmpty() || updatedPrice.isNaN() || amountOfCoin.isNaN()){ // trenger isNan?
                return@launch // Kan vel legge inn en melding til user her?
            }
            transactionDao.insert(Transactions(coinName = coinName, updatedPrice = updatedPrice, amountOfCoin = amountOfCoin))
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
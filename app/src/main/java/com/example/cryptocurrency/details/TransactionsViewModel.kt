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
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class TransactionsViewModel : ViewModel() {

    private lateinit var transactionDao: TransactionsDAO

    fun init(context: Context){
        transactionDao = DataBase.getDatabase(context).getTransactionDAO()
    }

    fun save(coinId: String, coinName: String, updatedPrice: Float, amountOfCoin: Float){
        viewModelScope.launch{
            val zoneId = ZoneId.of("Europe/Oslo")
            val zoneDateTime: ZonedDateTime = ZonedDateTime.now(zoneId)
            val currTime = zoneDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))

            transactionDao.insert(Transactions(coinId = coinId, coinName = coinName, updatedPrice = updatedPrice, amountOfCoin = amountOfCoin, transactionDate = currTime))
        }
    }

    /*fun update(transactionsId: Long,coinId: String, coinName: String?, updatedPrice: Float, amountOfCoin: Float){
        viewModelScope.launch{
            if(coinName.isNullOrEmpty()){ // updatedPrice.isNullOrEmpty? isNan?
                return@launch // Kan vel legge inn en melding til user her?
            }
            transactionDao.update(Transactions(transactionsId = transactionsId,coinId = coinId, coinName = coinName, updatedPrice = updatedPrice, amountOfCoin = amountOfCoin, transactionDate = Calendar.getInstance().time.toString()))
        }
    }*/
}
package com.example.cryptocurrency.list

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptocurrency.database.DataBase
import com.example.cryptocurrency.database.TransactionsDAO
import com.example.cryptocurrency.entities.Transactions
import kotlinx.coroutines.launch

class TransactionsListViewModel : ViewModel() {

    private val _transactionListLiveData : MutableLiveData<List<Transactions>> = MutableLiveData()
    val transactionListLiveData : LiveData<List<Transactions>> = _transactionListLiveData

    private val _sumAmountOfCoinsListLiveData : MutableLiveData<List<Float>> = MutableLiveData()
    val sumAmountOfCoinsListLiveData : LiveData<List<Float>> = _sumAmountOfCoinsListLiveData

    private lateinit var transactionDao: TransactionsDAO

    fun init(context: Context){
        transactionDao = DataBase.getDatabase(context).getTransactionDAO()
        //fetchData()
    }

    fun fetchAllData(){
        viewModelScope.launch {
            val list = transactionDao.fetchData()
            _transactionListLiveData.value = list
        }
    }

    fun fetchDataByCurrencySymbolName(coinName: String){
        viewModelScope.launch {
            val list = transactionDao.fetchDataByCurrencySymbolName(coinName)
            _transactionListLiveData.value = list
        }
    }

    fun fetchSumAmountByCoinName(coinName: String){
        viewModelScope.launch {
            val list = transactionDao.fetchSumAmountByCoinName(coinName)
            _sumAmountOfCoinsListLiveData.value = list
        }
    }

    /*fun fetchTotalAmountOfCoinsPerCoin(){
        viewModelScope.launch {
            val list = transactionDao.fetchTotalAmountOfCoinsPerCoin()
            _transactionListLiveData.value = list
        }
    }*/

    /*fun fetchAmountAndPriceFromTransactions(){
        viewModelScope.launch {
            val list = transactionDao.fetchAmountAndPriceFromTransactions()
            _transactionListLiveData.value = list
        }
    }*/

    fun deleteData(){
        viewModelScope.launch {
            transactionDao.deleteAll()
        }
    }


}
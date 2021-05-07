package com.example.cryptocurrency.list

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptocurrency.database.DataBase
import com.example.cryptocurrency.database.TransactionsDAO
import com.example.cryptocurrency.entities.GroupedSumAndNameTransaction
import com.example.cryptocurrency.entities.Transactions
import kotlinx.coroutines.launch

class TransactionsListViewModel : ViewModel() {

    private val _transactionListLiveData : MutableLiveData<List<Transactions>> = MutableLiveData()
    val transactionListLiveData : LiveData<List<Transactions>> = _transactionListLiveData

    private val _transactionListGroupedLiveData : MutableLiveData<List<GroupedSumAndNameTransaction>> = MutableLiveData()
    val transactionListGroupedLiveData : LiveData<List<GroupedSumAndNameTransaction>> = _transactionListGroupedLiveData

    private val _sumAmountOfCoinsByNameLiveData : MutableLiveData<Float> = MutableLiveData()
    val sumAmountOfCoinsByNameLiveData : LiveData<Float> = _sumAmountOfCoinsByNameLiveData

    private val _sumBalance : MutableLiveData<Float> = MutableLiveData()
    val sumBalance : LiveData<Float> = _sumBalance

    private lateinit var transactionDao: TransactionsDAO

    fun init(context: Context){
        transactionDao = DataBase.getDatabase(context).getTransactionDAO()
    }

    fun fetchAllData(){
        viewModelScope.launch {
            val list = transactionDao.fetchData()
            _transactionListLiveData.value = list
        }
    }

    fun fetchAmountOfCoinsByName(coinName: String){
        viewModelScope.launch {
            val list = transactionDao.fetchSumAmountByCoinName(coinName)
            if(list == null){
                _sumAmountOfCoinsByNameLiveData.value = 0F
            } else{
                _sumAmountOfCoinsByNameLiveData.value = list
            }
        }
    }

    fun fetchSumBalance(){
        viewModelScope.launch {
            val list = transactionDao.fetchSumBalance()
            if(list == null){
                _sumBalance.value = 0F
            } else{
                _sumBalance.value = list
            }
        }
    }

    fun fetchTransactionsGrouped(){
        viewModelScope.launch {
            val list = transactionDao.fetchTransactionsGrouped()
            _transactionListGroupedLiveData.value = list
        }
    }
}
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

    private lateinit var transactionDao: TransactionsDAO

    fun init(context: Context){
        transactionDao = DataBase.getDatabase(context).getTransactionDAO()
        fetchData()
    }

    fun fetchData(){
        viewModelScope.launch {
            val list = transactionDao.fetchData()
            _transactionListLiveData.value = list
        }
    }

}
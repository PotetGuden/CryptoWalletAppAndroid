package com.example.cryptocurrency

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {
    // annet navn?
    val cryptoService = API.cryptoService

    private val _totalCurrencies = MutableLiveData<Int>()
    val totalCurrencies: LiveData<Int> get() = _totalCurrencies // LiveData er immutable (kan ikke endres)

    private val _error = MutableLiveData<Unit>() // Evt boolean
    val error = MutableLiveData<Boolean>()

    private val exceptionHandler = CoroutineExceptionHandler { _, _-> // kan bruke _ for å ignorere values
        _error.postValue(Unit) // Evt true
    }

    private val _allCurrencies2 = MutableLiveData<AllCurrencies>()
    val allCurrencies2: LiveData<AllCurrencies> get() = _allCurrencies2

    init {
        LoadBitcoin()
        LoadCoinFromList()
        //LoadCoinByName("cardano")
    }

    val bitcoin = MutableLiveData<SingleCoin>()
    fun LoadBitcoin(){
        viewModelScope.launch(Dispatchers.IO + exceptionHandler){  //Coroutines
            val coin = cryptoService.getBitcoin()
            bitcoin.postValue(coin)
        }
    }

    private val _allCurrencies = MutableLiveData<AllCurrencies>()
    val allCurrencies: LiveData<AllCurrencies> get() = _allCurrencies
    fun LoadCoinFromList(){
        viewModelScope.launch(Dispatchers.IO){
            val currencies = cryptoService.getAllCurrencies()
            _totalCurrencies.postValue(currencies.data.lastIndex) // Sender med siste index i arrayet
            _allCurrencies.postValue(currencies)
        }
    }

    private val _specificCoin = MutableLiveData<SingleCoin>()
    val specificCoin: LiveData<SingleCoin> get() = _specificCoin
    fun LoadCoinByName(coinName: String){

        viewModelScope.launch(Dispatchers.IO){
            val specificCurrency = cryptoService.getAssetByName(coinName)
            _specificCoin.postValue(specificCurrency)
        }
    }

    fun UpdateValue(){
        viewModelScope.launch(Dispatchers.IO){
            val coin = cryptoService.getBitcoin()
            bitcoin.postValue(coin)
        }
    }


}

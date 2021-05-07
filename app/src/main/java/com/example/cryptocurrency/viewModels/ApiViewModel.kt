package com.example.cryptocurrency.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptocurrency.API
import com.example.cryptocurrency.AllChartData
import com.example.cryptocurrency.AllCurrencies
import com.example.cryptocurrency.SingleCoin
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ApiViewModel: ViewModel() {
    val cryptoService = API.cryptoService

    init {
        LoadCoinFromList()
    }

    private val _allCurrencies = MutableLiveData<AllCurrencies>()
    val allCurrencies: LiveData<AllCurrencies> get() = _allCurrencies
    fun LoadCoinFromList(){
        viewModelScope.launch(Dispatchers.IO){
            val currencies = cryptoService.getAllCurrencies()
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

    private val _specificChart = MutableLiveData<AllChartData>()
    val specificChart: LiveData<AllChartData> get() = _specificChart
    fun LoadChartById(currencyId: String){
        viewModelScope.launch(Dispatchers.IO){
            val specificChart = cryptoService.getChartById(currencyId)
            _specificChart.postValue(specificChart)
        }
    }
}

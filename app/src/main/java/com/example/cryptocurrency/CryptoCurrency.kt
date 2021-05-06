package com.example.cryptocurrency

import retrofit2.http.GET
import retrofit2.http.Path

interface CryptoCurrency {

    @GET("assets/bitcoin")
    suspend fun getBitcoin(): SingleCoin

    @GET("assets/{coinName}")
    suspend fun getAssetByName(@Path("coinName") coinName: String): SingleCoin

    @GET("assets")
    suspend fun getAllCurrencies(): AllCurrencies

    @GET("assets/{currencyId}/history?interval=d1")
    suspend fun getChartById(@Path("currencyId") currencyId: String): AllChartData

}
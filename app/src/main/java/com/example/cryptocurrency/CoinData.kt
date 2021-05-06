package com.example.cryptocurrency

import androidx.lifecycle.MutableLiveData
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AllCurrencies(
    val data: List<Coins>
    )

data class Coins(
    val id: String,
    val rank: String,
    val symbol: String,
    val name: String,
    val supply: String,
    val maxSupply: String?,
    val marketCapUsd: String,
    val volumeUsd24Hr: String,
    val priceUsd: String,
    val changePercent24Hr: String)

data class SingleCoin(
    val data: Coins
    )

data class AllChartData(
    val data: List<SingleChart>
)
data class SingleChart(
    val priceUsd: String,
    val time: String,
    val date: String
)




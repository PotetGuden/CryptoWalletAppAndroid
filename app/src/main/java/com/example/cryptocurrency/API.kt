package com.example.cryptocurrency

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

// STATISK -  ville kanskje gjort det om til en singleton eller (lifecycle?)

//Gjør at det er mulig å convertere fra JSON til kotlin object
private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

// Gjør at vi kan bruke GET/POST etc
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl("https://api.coincap.io/v2/")
    .build()

object API{
    val cryptoService: CryptoCurrency by lazy{
        retrofit.create(CryptoCurrency::class.java)
    }
}
package com.example.cryptocurrency

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


// Makes it possible to convert from JSON to Kotlin Objects
private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

// GET/POST requests etc
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl("https://api.coincap.io/v2/")
    .build()

object API{
    val cryptoService: CryptoCurrency by lazy{
        retrofit.create(CryptoCurrency::class.java)
    }
}
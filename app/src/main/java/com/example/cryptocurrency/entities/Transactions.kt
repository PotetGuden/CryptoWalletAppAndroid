package com.example.cryptocurrency.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions_table")
data class Transactions (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "transactionsId")
    val transactionsId: Long = 0, // PK
    @ColumnInfo(name = "coinId")
    val coinId: String,
    @ColumnInfo(name = "coinName")
    val coinName: String,
    @ColumnInfo(name = "updatedPrice")
    val updatedPrice: Float,
    @ColumnInfo(name = "amountOfCoin")
    val amountOfCoin: Float,
    @ColumnInfo(name = "transactionDate")
    val transactionDate: String
    )







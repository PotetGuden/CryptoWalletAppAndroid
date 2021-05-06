package com.example.cryptocurrency.database

import androidx.room.*
import com.example.cryptocurrency.entities.Transactions

@Dao
interface TransactionsDAO{
    @Insert
    suspend fun insert(transaction: Transactions)  // Suspend extends lifetime to coroutine

    @Update
    suspend fun update(transaction: Transactions)

    @Delete
    suspend fun delete(transaction: Transactions)

    @Query("SELECT * FROM transactions_table ORDER BY transactionsId")
    suspend fun fetchData() : List<Transactions>

    @Query("SELECT SUM(amountOfCoin) FROM transactions_table WHERE coinName = :coinName")
    suspend fun fetchSumAmountByCoinName(coinName: String) : Float?

    @Query("SELECT SUM(amountOfCoin*updatedPrice) FROM transactions_table")
    suspend fun fetchSumBalance() : Float?

    @Query("SELECT * FROM transactions_table GROUP BY coinName;")
    suspend fun fetchTransactionsGrouped() : List<Transactions>

    //@Query("SELECT * FROM transactions_table WHERE transactionsId = :id")
    //suspend fun fetchDataWithId(id: Long) : Transactions

    @Query("DELETE FROM transactions_table")
    suspend fun deleteAll()
}
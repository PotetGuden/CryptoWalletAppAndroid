package com.example.cryptocurrency.database

import androidx.room.*
import com.example.cryptocurrency.entities.Transactions

@Dao
interface TransactionsDAO{
    @Insert
    suspend fun insert(transaction: Transactions)  // suspend fordi databasen kjører aldri fra main thread - suspend extender lifetime til coroutine

    @Update
    suspend fun update(transaction: Transactions)

    @Delete
    suspend fun delete(transaction: Transactions)

    @Query("select * from transactions_table order by transactionsId")
    suspend fun fetchData() : List<Transactions>

    @Query("select * from transactions_table where transactionsId = :id")
    suspend fun fetchDataWithId(id : Long) : Transactions
}
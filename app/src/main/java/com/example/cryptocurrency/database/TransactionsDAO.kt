package com.example.cryptocurrency.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import com.example.cryptocurrency.entities.Transactions

@Dao
interface TransactionsDAO{
    @Insert
    suspend fun insert(transaction: Transactions)  // suspend fordi databasen kjører aldri fra main thread - suspend extender lifetime til coroutine

    @Update
    suspend fun update(transaction: Transactions)

    @Delete
    suspend fun delete(transaction: Transactions)

}
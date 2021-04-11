package com.example.cryptocurrency.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import com.example.cryptocurrency.entities.Transactions

@Dao
interface TransactionsDAO{
    @Insert
    fun insert(transaction: Transactions)

    @Update
    fun update(transaction: Transactions)

    @Delete
    fun delete(transaction: Transactions)

}
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

    @Query("SELECT * FROM transactions_table ORDER BY transactionsId")
    suspend fun fetchData() : List<Transactions>

    /*@Query("select updatedPrice,amountOfCoin from transactions_table order by transactionsId") // burde ikke trenge order by
    suspend fun fetchAmountAndPriceFromTransactions() : List<Transactions>*/
    @Query("SELECT * FROM transactions_table WHERE coinName = :coinName")
    suspend fun fetchDataByCurrencySymbolName(coinName: String) : List<Transactions>


    @Query("SELECT SUM(amountOfCoin) FROM transactions_table WHERE coinName = :coinName")
    suspend fun fetchSumAmountByCoinName(coinName: String) : Float


    @Query("SELECT SUM(amountOfCoin) AS totalCoins FROM transactions_table GROUP BY coinName HAVING SUM(amountOfCoin) > 0;")
    suspend fun fetchTotalAmountOfCoinsPerCoin() : List<Float>

    @Query("SELECT coinName FROM transactions_table GROUP BY coinName HAVING SUM(amountOfCoin) > 0;")
    suspend fun fetchNameAmountOfCoins() : List<String>


    @Query("SELECT * FROM transactions_table WHERE transactionsId = :id")
    suspend fun fetchDataWithId(id: Long) : Transactions

    @Query("DELETE FROM transactions_table")
    suspend fun deleteAll()
}
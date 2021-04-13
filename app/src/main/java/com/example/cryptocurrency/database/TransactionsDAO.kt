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

    /*@Query("select updatedPrice,amountOfCoin from transactions_table order by transactionsId") // burde ikke trenge order by
    suspend fun fetchAmountAndPriceFromTransactions() : List<Transactions>*/
    @Query("select * from transactions_table where coinName = :coinName")
    suspend fun fetchDataByCurrencySymbolName(coinName: String) : List<Transactions>


    @Query("select SUM(amountOfCoin) from transactions_table where coinName = :coinName")
    suspend fun fetchSumAmountByCoinName(coinName: String) : List<Float>



    @Query("select SUM(amountOfCoin) as totalCoins from transactions_table group by coinName having SUM(amountOfCoin) > 0;")
    suspend fun fetchTotalAmountOfCoinsPerCoin() : List<Float>

    @Query("select coinName from transactions_table group by coinName having SUM(amountOfCoin) > 0;")
    suspend fun fetchNameAmountOfCoins() : List<String>




    @Query("select * from transactions_table where transactionsId = :id")
    suspend fun fetchDataWithId(id: Long) : Transactions

    @Query("DELETE FROM transactions_table")
    suspend fun deleteAll()
}
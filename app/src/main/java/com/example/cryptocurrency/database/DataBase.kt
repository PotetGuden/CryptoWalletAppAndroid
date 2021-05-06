package com.example.cryptocurrency.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.cryptocurrency.entities.Transactions

const val DATABASE_NAME: String = "transactions_database"

// Singleton database
@Database(entities = [Transactions::class], version = 1) // Room handles which version
abstract class DataBase : RoomDatabase(){
    abstract fun getTransactionDAO() : TransactionsDAO

    companion object{

        var db : DataBase? = null

        fun getDatabase(context: Context) : DataBase{ // Should maybe have threadlocking here?

            val newDB = db?: Room.databaseBuilder(context,
                DataBase::class.java, DATABASE_NAME).build()
            return newDB.also {
                db = it
            }
        }
    }
}
package com.josegonzalez.jetpackCrypto.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.josegonzalez.jetpackCrypto.data.local.dao.CryptoDao
import com.josegonzalez.jetpackCrypto.data.local.entity.CoinEntity

@Database(entities = [CoinEntity::class], version = 1, exportSchema = false)
abstract class CryptoDatabase : RoomDatabase() {

    abstract fun cryptoDao(): CryptoDao

    companion object {
        @Volatile private var INSTANCE: CryptoDatabase? = null

        fun getInstance(context: Context): CryptoDatabase =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(context, CryptoDatabase::class.java, "crypto_db")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
    }
}
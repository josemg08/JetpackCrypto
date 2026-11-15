package com.josegonzalez.jetpackCrypto.di

import android.content.Context
import com.josegonzalez.jetpackCrypto.data.local.dao.CryptoDao
import com.josegonzalez.jetpackCrypto.data.local.database.CryptoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): CryptoDatabase {
        return CryptoDatabase.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideCryptoDao(database: CryptoDatabase): CryptoDao {
        return database.cryptoDao()
    }
}

package com.josegonzalez.jetpackCrypto.di

import com.josegonzalez.jetpackCrypto.data.local.dao.CryptoDao
import com.josegonzalez.jetpackCrypto.data.remote.api.CryptoApiService
import com.josegonzalez.jetpackCrypto.data.repository.CryptoRepositoryImpl
import com.josegonzalez.jetpackCrypto.domain.repository.CryptoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideCryptoRepository(
        apiService: CryptoApiService,
        dao: CryptoDao
    ): CryptoRepository {
        return CryptoRepositoryImpl(apiService, dao)
    }
}

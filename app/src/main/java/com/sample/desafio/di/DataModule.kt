package com.sample.desafio.di

import com.sample.desafio.domain.repository.HitRepository
import com.sample.desafio.data.repository.HitRepositoryData
import com.sample.desafio.data.datasource.HitRemoteDataSource
import com.sample.desafio.data.datasource.remote.RetrofitDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    abstract fun provideHitRepository(repository: HitRepositoryData): HitRepository


    @Binds
    abstract fun provideHitDataSource(datasource: RetrofitDataSource): HitRemoteDataSource
}
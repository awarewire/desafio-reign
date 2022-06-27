package com.sample.desafio.di

import android.content.Context
import androidx.room.Room
import com.sample.desafio.data.datasource.local.db.AppDatabase
import com.sample.desafio.data.datasource.local.db.HitsDao
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
    fun providesNiaDatabase(
        @ApplicationContext context: Context,
    ): AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "app-database"
    ).build()


    @Provides
    fun providesHitsDao(
        database: AppDatabase,
    ): HitsDao = database.hitsDao()
}

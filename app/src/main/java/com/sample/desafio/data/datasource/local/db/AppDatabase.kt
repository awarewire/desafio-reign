package com.sample.desafio.data.datasource.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [HitEntity::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(
    InstantConverter::class
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun hitsDao(): HitsDao
}
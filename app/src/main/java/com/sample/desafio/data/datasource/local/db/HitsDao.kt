package com.sample.desafio.data.datasource.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HitsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: HitEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entities: List<HitEntity>): List<Long>

    @Query("UPDATE hits SET deleted = :deleted WHERE id = :id")
    suspend fun updateStatus(id: String, deleted: Boolean = true)

    @Query(value = """SELECT * FROM hits WHERE deleted = :deleted ORDER BY dateCreated DESC;""")
    fun getHitsStream(deleted: Boolean = false): Flow<List<HitEntity>>

    @Query(value = """SELECT id FROM hits WHERE deleted=:deleted;""")
    fun getAllIdHitsDeleted(deleted: Boolean = true): List<String>
}
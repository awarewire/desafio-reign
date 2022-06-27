package com.sample.desafio.data.datasource.local.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sample.desafio.domain.HitDomain
import kotlinx.datetime.Instant

@Entity(tableName = "hits")
data class HitEntity(
    @PrimaryKey val id: String,
    val title: String,
    val dateCreated: Instant,
    val author: String,
    val url: String,
    val deleted: Boolean = false
)

fun List<HitEntity>.toListDomain() = this.map { entity -> entity.toDomain() }
fun HitEntity.toDomain() = HitDomain(
    id = id,
    title = title,
    dateCreated = dateCreated.toEpochMilliseconds(),
    author = author,
    url = url
)

fun List<HitDomain>.toListEntity() = this.map { model -> model.toEntity() }
fun HitDomain.toEntity() = HitEntity(
    id = id,
    title = title,
    dateCreated = Instant.fromEpochMilliseconds(dateCreated),
    author = author,
    url = url
)
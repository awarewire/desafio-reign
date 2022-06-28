package com.sample.desafio.utils

import com.sample.desafio.data.datasource.local.db.toEntity
import com.sample.desafio.data.datasource.remote.model.HitModel
import com.sample.desafio.data.datasource.remote.model.HitResponseModel
import com.sample.desafio.data.datasource.remote.model.toDomain

object FakeData {

    fun getFakeHitModel() = HitModel(
        id = "test",
        title = "test",
        titleFallback = null,
        dateCreated = "2022-06-28T18:43:00Z",
        author = "test",
        url = "test",
    )

    fun getFakeHitResponseModel() = HitResponseModel(
        hits = listOf(getFakeHitModel())
    )

    fun getFakeHitDomain() = getFakeHitModel().toDomain()
    fun getFakeHitEntity() = getFakeHitModel().toDomain().toEntity()
}
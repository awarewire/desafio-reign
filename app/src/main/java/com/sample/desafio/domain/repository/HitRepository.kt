package com.sample.desafio.domain.repository

import com.sample.desafio.domain.HitDomain
import com.sample.desafio.domain.commons.functional.Either
import com.sample.desafio.domain.commons.functional.Failure
import kotlinx.coroutines.flow.Flow

interface HitRepository {

    suspend fun getHits(): Either<Failure, List<HitDomain>>

    suspend fun deleteHit(id: String): Either<Failure, Unit>

    fun getHitsStream(): Flow<List<HitDomain>>
}
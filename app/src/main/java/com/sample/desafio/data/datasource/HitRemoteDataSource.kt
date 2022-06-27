package com.sample.desafio.data.datasource

import com.sample.desafio.domain.HitDomain
import com.sample.desafio.domain.commons.functional.Either
import com.sample.desafio.domain.commons.functional.Failure

interface HitRemoteDataSource {
    suspend fun getHits(): Either<Failure, List<HitDomain>>
}
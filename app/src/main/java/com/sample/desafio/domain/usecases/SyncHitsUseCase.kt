package com.sample.desafio.domain.usecases

import com.sample.desafio.domain.commons.UseCase
import com.sample.desafio.domain.commons.functional.Either
import com.sample.desafio.domain.commons.functional.Failure
import com.sample.desafio.domain.commons.functional.map
import com.sample.desafio.domain.repository.HitRepository
import javax.inject.Inject

class SyncHitsUseCase @Inject constructor(
    private val repository: HitRepository
) : UseCase<Unit, Unit?> {
    override suspend fun run(params: Unit?): Either<Failure, Unit> {
        return repository.getHits().map { }
    }

}
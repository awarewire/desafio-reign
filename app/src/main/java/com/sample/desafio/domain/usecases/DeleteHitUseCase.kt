package com.sample.desafio.domain.usecases

import com.sample.desafio.domain.repository.HitRepository
import com.sample.desafio.domain.commons.UseCase
import com.sample.desafio.domain.commons.functional.Either
import com.sample.desafio.domain.commons.functional.Failure
import javax.inject.Inject

class DeleteHitUseCase @Inject constructor(
    private val repository: HitRepository
) : UseCase<Unit, String> {
    override suspend fun run(params: String): Either<Failure, Unit> {
        return repository.deleteHit(params)
    }

}
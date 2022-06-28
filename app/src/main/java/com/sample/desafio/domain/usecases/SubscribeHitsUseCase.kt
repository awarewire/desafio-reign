package com.sample.desafio.domain.usecases

import com.sample.desafio.domain.HitDomain
import com.sample.desafio.domain.commons.ObservableUseCase
import com.sample.desafio.domain.commons.functional.Either
import com.sample.desafio.domain.commons.functional.Failure
import com.sample.desafio.domain.repository.HitRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SubscribeHitsUseCase @Inject constructor(
    private val repository: HitRepository
) : ObservableUseCase<List<HitDomain>, Unit?> {

    override fun run(params: Unit?): Flow<Either<Failure, List<HitDomain>>> {
        return repository.getHitsStream()
    }

}
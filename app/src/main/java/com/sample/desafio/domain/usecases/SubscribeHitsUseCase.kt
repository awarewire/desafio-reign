package com.sample.desafio.domain.usecases

import com.sample.desafio.domain.repository.HitRepository
import com.sample.desafio.domain.HitDomain
import com.sample.desafio.domain.commons.ObservableUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SubscribeHitsUseCase @Inject constructor(
    private val repository: HitRepository
) : ObservableUseCase<List<HitDomain>, Unit?> {

    override fun run(params: Unit?): Flow<List<HitDomain>> {
        return repository.getHitsStream()
    }

}
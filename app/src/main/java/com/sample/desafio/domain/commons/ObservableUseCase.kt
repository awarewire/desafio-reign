package com.sample.desafio.domain.commons

import com.sample.desafio.domain.commons.functional.Either
import com.sample.desafio.domain.commons.functional.Failure
import kotlinx.coroutines.flow.Flow

interface ObservableUseCase<out Type, in Params> {

    fun run(params: Params): Flow<Either<Failure, Type>>
}

package com.sample.desafio.domain.commons

import com.sample.desafio.domain.commons.functional.Either
import com.sample.desafio.domain.commons.functional.Failure

interface UseCase<out Type, in Params> {

    suspend fun run(params: Params): Either<Failure, Type>
}
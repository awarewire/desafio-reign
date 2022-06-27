package com.sample.desafio.domain.commons

import com.sample.desafio.domain.commons.functional.Either
import com.sample.desafio.domain.commons.functional.Failure
import com.sample.desafio.domain.commons.functional.GenericUseCaseError
import kotlinx.coroutines.*

interface UseCaseExecutor {
    operator fun <T, P> UseCase<T, P>.invoke(
        scope: CoroutineScope,
        params: P,
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        onResult: (Either<Failure, T>) -> Unit = {}
    )

    operator fun <T, P> ObservableUseCase<T, P>.invoke(
        scope: CoroutineScope,
        params: P,
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        onResult: (Either<Failure, T>) -> Unit = {}
    )
}

class DefaultUseCaseExecutor : UseCaseExecutor {

    override operator fun <T, P> UseCase<T, P>.invoke(
        scope: CoroutineScope,
        params: P,
        dispatcher: CoroutineDispatcher,
        onResult: (Either<Failure, T>) -> Unit
    ) {
        val backgroundJob = scope.async(dispatcher) { run(params) }
        scope.launch {
            onResult(backgroundJob.await())
        }
    }

    override operator fun <T, P> ObservableUseCase<T, P>.invoke(
        scope: CoroutineScope,
        params: P,
        dispatcher: CoroutineDispatcher,
        onResult: (Either<Failure, T>) -> Unit
    ) {
        val backgroundJob = scope.async(dispatcher) { run(params) }
        scope.launch {
            try {
                backgroundJob.await().collect { onResult(Either.Right(it)) }
            } catch (e: Throwable) {
                onResult(Either.Left(GenericUseCaseError(e)))
            }
        }
    }
}

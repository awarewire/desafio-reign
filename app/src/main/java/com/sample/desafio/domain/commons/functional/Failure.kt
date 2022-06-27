package com.sample.desafio.domain.commons.functional

sealed class Failure

sealed class NetworkFailure : Failure()

object NetworkConnection : NetworkFailure()
object ServerError : NetworkFailure()
object Cancelled : NetworkFailure()

object EmptyResponseBody : NetworkFailure()

data class GenericUseCaseError(val throwable: Throwable) : Failure()
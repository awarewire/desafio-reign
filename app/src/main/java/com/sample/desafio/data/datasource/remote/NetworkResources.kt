package com.sample.desafio.data.datasource.remote

import com.sample.desafio.data.device.NetworkHandler
import com.sample.desafio.domain.commons.functional.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

abstract class ApiService {
    abstract val networkHandler: NetworkHandler

    suspend fun <T> request(
        default: T? = null,
        call: suspend () -> Response<T>
    ): Either<Failure, T> =
        withContext(Dispatchers.IO) {
            return@withContext when (networkHandler.isConnected()) {
                true -> performRequest(call, default)
                false -> Either.Left(NetworkConnection)
            }
        }

    private suspend fun <T> performRequest(
        call: suspend () -> Response<T>,
        default: T? = null
    ): Either<Failure, T> {
        return try {
            val response = call()
            if (response.isSuccessful) {
                response.body()?.let { Either.Right(it) }
                    ?: (default?.let { Either.Right(it) } ?: Either.Left(EmptyResponseBody))
            } else {
                Either.Left(ServerError)
            }
        } catch (exception: Throwable) {
            when (exception) {
                is CancellationException -> Either.Left(Cancelled)
                else -> Either.Left(ServerError)
            }
        }
    }
}
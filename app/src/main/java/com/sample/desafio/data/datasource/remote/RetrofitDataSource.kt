package com.sample.desafio.data.datasource.remote

import com.sample.desafio.data.datasource.HitRemoteDataSource
import com.sample.desafio.data.device.NetworkHandler
import com.sample.desafio.data.datasource.remote.model.toListDomain
import com.sample.desafio.domain.HitDomain
import com.sample.desafio.domain.commons.functional.Either
import com.sample.desafio.domain.commons.functional.Failure
import com.sample.desafio.domain.commons.functional.map
import javax.inject.Inject

class RetrofitDataSource @Inject constructor(
    private val api: AppApi,
    override val networkHandler: NetworkHandler
) : ApiService(), HitRemoteDataSource {

    override suspend fun getHits(): Either<Failure, List<HitDomain>> {
        return request {
            api.getHits()
        }.map { model -> model.hits.orEmpty().toListDomain() }
    }
}
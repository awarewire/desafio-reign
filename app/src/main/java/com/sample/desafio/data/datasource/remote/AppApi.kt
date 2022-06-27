package com.sample.desafio.data.datasource.remote

import com.sample.desafio.data.datasource.remote.model.HitResponseModel
import retrofit2.Response
import retrofit2.http.GET

interface AppApi {

    @GET("/api/v1/search_by_date?query=mobile")
    suspend fun getHits(): Response<HitResponseModel>
}
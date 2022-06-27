@file:OptIn(ExperimentalCoroutinesApi::class)

package com.sample.desafio.data.datasource.remote

import com.nhaarman.mockitokotlin2.whenever
import com.sample.desafio.data.device.NetworkHandler
import com.sample.desafio.data.datasource.remote.model.HitResponseModel
import com.sample.desafio.domain.commons.functional.Either
import com.sample.desafio.domain.commons.functional.NetworkConnection
import com.sample.desafio.domain.commons.functional.ServerError
import com.sample.desafio.utils.FakeData.getFakeHitResponseModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class RetrofitDataSourceTest {

    @Mock
    lateinit var mockAppApi: AppApi

    @Mock
    lateinit var mockNetworkHandler: NetworkHandler

    @InjectMocks
    lateinit var sutRetrofitDataSource: RetrofitDataSource

    @Test
    fun `given hits when getHits then success Either returned`() {
        runTest {
            val hitsResponse = getFakeHitResponseModel()
            val response = Response.success(hitsResponse)
            whenever(mockAppApi.getHits()).thenReturn(response)
            whenever(mockNetworkHandler.isConnected()).thenReturn(true)

            val result = sutRetrofitDataSource.getHits()

            Assert.assertEquals(result.isRight, true)
            Assert.assertEquals((result as Either.Right).b.size, hitsResponse.hits!!.size)
        }
    }

    @Test
    fun `given error when getHits then error Either returned`() {
        runTest {
            val response = Response.error<HitResponseModel>(404, "".toResponseBody())
            whenever(mockAppApi.getHits()).thenReturn(response)
            whenever(mockNetworkHandler.isConnected()).thenReturn(true)

            val result = sutRetrofitDataSource.getHits()

            Assert.assertEquals(result.isLeft, true)
            Assert.assertEquals((result as Either.Left).a, ServerError)
        }
    }

    @Test
    fun `given not network when getHits then error Either returned`() {
        runTest {
            whenever(mockNetworkHandler.isConnected()).thenReturn(false)

            val result = sutRetrofitDataSource.getHits()

            Assert.assertEquals(result.isLeft, true)
            Assert.assertEquals((result as Either.Left).a, NetworkConnection)
        }
    }
}
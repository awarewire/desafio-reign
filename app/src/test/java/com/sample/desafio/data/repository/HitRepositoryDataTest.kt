@file:OptIn(ExperimentalCoroutinesApi::class)

package com.sample.desafio.data.repository

import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.sample.desafio.data.datasource.HitRemoteDataSource
import com.sample.desafio.data.datasource.local.db.HitsDao
import com.sample.desafio.data.datasource.local.db.toListEntity
import com.sample.desafio.data.datasource.remote.model.toListDomain
import com.sample.desafio.domain.commons.functional.Either
import com.sample.desafio.utils.FakeData
import com.sample.desafio.utils.FakeData.getFakeHitDomain
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class HitRepositoryDataTest {
    @Mock
    lateinit var mockHitRemoteDataSource: HitRemoteDataSource

    @Mock
    lateinit var mockHitsDao: HitsDao

    @InjectMocks
    lateinit var sutHitRepositoryData: HitRepositoryData

    @Test
    fun `given hits when getHits then success Either returned, and data saved at room`() {
        runTest {
            val hits = FakeData.getFakeHitResponseModel().hits!!.toListDomain()
            val response = Either.Right(hits)
            val ids = listOf<String>()
            whenever(mockHitRemoteDataSource.getHits()).thenReturn(response)
            whenever(mockHitsDao.getAllIdHitsDeleted()).thenReturn(ids)

            val result = sutHitRepositoryData.getHits()

            assertEquals(result.isRight, true)
            verify(mockHitsDao).getAllIdHitsDeleted()
            verify(mockHitsDao).insert(hits.toListEntity())
        }
    }

    @Test
    fun `given hits when getHitsStream then dao data equals result domain data`() =
        runTest {
            val hitsDomain = listOf(getFakeHitDomain(), getFakeHitDomain())
            whenever(mockHitsDao.getHitsStream()).thenReturn(
                flow { emit(hitsDomain.toListEntity()) }
            )
            val domainStream = sutHitRepositoryData.getHitsStream().first()
            assertEquals(hitsDomain, domainStream)
        }

    @Test
    fun `given hits when deleteHit then success Either returned, and data update at room`() {
        runTest {
            val id = "test"
            whenever(mockHitsDao.updateStatus(id)).thenReturn(Unit)

            val result = sutHitRepositoryData.deleteHit(id)

            assertEquals(result.isRight, true)
            verify(mockHitsDao).updateStatus(id)
        }
    }
}
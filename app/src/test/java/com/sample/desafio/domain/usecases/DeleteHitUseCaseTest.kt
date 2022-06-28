@file:OptIn(ExperimentalCoroutinesApi::class)

package com.sample.desafio.domain.usecases

import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.sample.desafio.domain.commons.functional.Either
import com.sample.desafio.domain.commons.functional.ServerError
import com.sample.desafio.domain.repository.HitRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DeleteHitUseCaseTest {

    @Mock
    lateinit var mockHitRepository: HitRepository

    @InjectMocks
    lateinit var sutDeleteHitUseCase: DeleteHitUseCase

    @Test
    fun `given hits when getHits then success Either returned`() {
        runTest {
            val id = "test"
            val response = Either.Right(Unit)
            whenever(mockHitRepository.deleteHit(id)).thenReturn(response)

            val result = sutDeleteHitUseCase.run(id)

            Assert.assertEquals(result.isRight, true)
            verify(mockHitRepository).deleteHit(id)
        }
    }

    @Test
    fun `given hits when getHits then error Either returned`() {
        runTest {
            val id = "test"
            val response = Either.Left(ServerError)
            whenever(mockHitRepository.deleteHit(id)).thenReturn(response)

            val result = sutDeleteHitUseCase.run(id)

            Assert.assertEquals(result.isLeft, true)
            verify(mockHitRepository).deleteHit(id)
        }
    }
}
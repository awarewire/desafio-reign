@file:OptIn(ExperimentalCoroutinesApi::class)

package com.sample.desafio.presentation.hits

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.sample.desafio.domain.commons.functional.Either
import com.sample.desafio.domain.usecases.DeleteHitUseCase
import com.sample.desafio.domain.usecases.SubscribeHitsUseCase
import com.sample.desafio.domain.usecases.SyncHitsUseCase
import com.sample.desafio.utils.FakeData.getFakeHitDomain
import com.sample.desafio.utils.getOrAwaitValue
import com.sample.desafio.utils.observeForTesting
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class HitsViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var mockSubscribeHitsUseCase: SubscribeHitsUseCase

    @Mock
    lateinit var mockSyncHitsUseCase: SyncHitsUseCase

    @Mock
    lateinit var mockDeleteUseCase: DeleteHitUseCase

    @InjectMocks
    lateinit var sutHitsViewModel: HitsViewModel

    @Before
    fun setup() {
        sutHitsViewModel = HitsViewModel(
            mockSubscribeHitsUseCase,
            mockSyncHitsUseCase, mockDeleteUseCase
        )
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when refreshData then mockSyncHitsUseCase is called`() {
        runTest {
            val response = Either.Right(Unit)
            whenever(mockSyncHitsUseCase.run(Unit)).thenReturn(response)

            sutHitsViewModel.refreshData()

            advanceUntilIdle()
            verify(mockSyncHitsUseCase).run(Unit)
        }
    }

    @Test
    fun `given id when removeItem then mockDeleteUseCase is called `() {
        runTest {
            val id = "test"
            val response = Either.Right(Unit)
            whenever(mockDeleteUseCase.run(id)).thenReturn(response)

            sutHitsViewModel.removeItem(id)

            advanceUntilIdle()
            verify(mockDeleteUseCase).run(id)
        }
    }

    @Test
    fun `when subscribeHits then mockSubscribeHitsUseCase is called `() {
        runTest {
            sutHitsViewModel.mainStateUi.observeForTesting {
                val hitsDomain = listOf(getFakeHitDomain(), getFakeHitDomain())
                val mainStateUi = MainStateUi.DisplayHits(hitsDomain.toListStateUi())
                val response = flow { emit(Either.Right(hitsDomain)) }
                whenever(mockSubscribeHitsUseCase.run(Unit)).thenReturn(response)

                sutHitsViewModel.subscribeHits()

                advanceUntilIdle()
                val hitsUiState = sutHitsViewModel.mainStateUi.getOrAwaitValue()
                verify(mockSubscribeHitsUseCase).run(Unit)
                Assert.assertEquals(mainStateUi, hitsUiState)
            }
        }
    }
}
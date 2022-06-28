package com.sample.desafio.presentation.hits

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.desafio.domain.commons.DefaultUseCaseExecutor
import com.sample.desafio.domain.commons.UseCaseExecutor
import com.sample.desafio.domain.commons.functional.onFailure
import com.sample.desafio.domain.commons.functional.onSuccess
import com.sample.desafio.domain.usecases.DeleteHitUseCase
import com.sample.desafio.domain.usecases.SubscribeHitsUseCase
import com.sample.desafio.domain.usecases.SyncHitsUseCase
import com.sample.desafio.presentation.commons.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HitsViewModel @Inject constructor(
    private val subscribeHitsUseCase: SubscribeHitsUseCase,
    private val syncHitsUseCase: SyncHitsUseCase,
    private val deleteHitUseCase: DeleteHitUseCase
) : ViewModel(), UseCaseExecutor by DefaultUseCaseExecutor() {

    private val _mainStateUi = MutableLiveData<MainStateUi>()
    val mainStateUi: LiveData<MainStateUi> = _mainStateUi

    private val _navigationUi = MutableLiveData<Event<MainNavigateUi>>()
    val navigationUi: LiveData<Event<MainNavigateUi>> = _navigationUi

    fun initViewModel() {
        subscribeHits()
        refreshData()
    }

    @VisibleForTesting
    fun subscribeHits() {
        subscribeHitsUseCase(viewModelScope, Unit) { either ->
            either.onSuccess { hitsDomain ->
                _mainStateUi.value = MainStateUi.DisplayHits(hitsDomain.toListStateUi())
            }.onFailure {
                _mainStateUi.value = MainStateUi.ErrorNetwork
            }
        }
    }

    fun refreshData() {
        syncHitsUseCase(viewModelScope, Unit)
    }

    fun removeItem(id: String) {
        deleteHitUseCase(viewModelScope, id)
    }

    fun navigateToDetails(hit: HitStateUi) {
        if (hit.url.isEmpty()) {
            _navigationUi.value = Event.buildEvent(MainNavigateUi.NotFoundUrl)
        } else {
            _navigationUi.value = Event.buildEvent(MainNavigateUi.GoDetails(hit))
        }
    }
}
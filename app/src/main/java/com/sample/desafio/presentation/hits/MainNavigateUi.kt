package com.sample.desafio.presentation.hits

sealed class MainNavigateUi {
    data class GoDetails(val hist: HitStateUi) : MainNavigateUi()
    object NotFoundUrl : MainNavigateUi()
}
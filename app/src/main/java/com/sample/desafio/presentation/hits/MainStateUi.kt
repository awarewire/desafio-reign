package com.sample.desafio.presentation.hits

sealed class MainStateUi {
    data class DisplayHits(val hist: List<HitStateUi>) : MainStateUi()
    object ErrorNetwork : MainStateUi()
}
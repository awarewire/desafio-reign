package com.sample.desafio.domain

data class HitDomain(
    val id: String,
    val title: String,
    val dateCreated: Long,
    val author: String,
    val url: String
)
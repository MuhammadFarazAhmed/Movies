package com.example.domain.model

data class Media(
    val id: Int,
    val name: String?,
    val description: String?,
    val video: Boolean?,
    val backdropPath: String?,
    val mediaType: String,
    val adult: Boolean?
)


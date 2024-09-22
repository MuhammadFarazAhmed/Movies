package com.example.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Media(
    val id: Int = -1,
    val name: String? = "",
    val description: String? = "",
    val video: Boolean? = false,
    val backdropPath: String? = "",
    val mediaType: String = "",
    val adult: Boolean? = false
)
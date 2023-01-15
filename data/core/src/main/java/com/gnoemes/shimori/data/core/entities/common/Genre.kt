package com.gnoemes.shimori.data.core.entities.common

@kotlinx.serialization.Serializable
data class Genre(
    val name: String = "",
    val nameRu: String? = null
)
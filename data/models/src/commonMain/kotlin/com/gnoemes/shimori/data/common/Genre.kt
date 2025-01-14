package com.gnoemes.shimori.data.common

@kotlinx.serialization.Serializable
data class Genre(
    val name: String = "",
    val nameRu: String? = null
)
package com.gnoemes.shimori.data.common

import com.gnoemes.shimori.data.ShimoriEntity

@kotlinx.serialization.Serializable
data class Genre(
    override val id: Long = 0,
    val sourceId: Long,
    val type: GenreType,
    val name: String = "",
    val nameRu: String? = null,
    val description: String? = null,
) : ShimoriEntity
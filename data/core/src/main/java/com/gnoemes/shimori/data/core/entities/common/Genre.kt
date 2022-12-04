package com.gnoemes.shimori.data.core.entities.common

import com.gnoemes.shimori.data.core.entities.ShikimoriEntity

@kotlinx.serialization.Serializable
data class Genre(
    override val shikimoriId: Long = 0,
    val name: String = "",
    val nameRu: String? = null
) : ShikimoriEntity
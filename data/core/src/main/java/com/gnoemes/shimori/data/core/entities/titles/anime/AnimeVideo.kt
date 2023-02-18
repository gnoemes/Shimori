package com.gnoemes.shimori.data.core.entities.titles.anime

import com.gnoemes.shimori.data.core.entities.ShimoriEntity

@kotlinx.serialization.Serializable
data class AnimeVideo(
    override val id: Long = 0,
    val titleId: Long = 0,
    val name: String? = null,
    val url: String = "",
    val imageUrl: String? = null,
    val type: AnimeVideoType,
    val hosting: String? = null
) : ShimoriEntity
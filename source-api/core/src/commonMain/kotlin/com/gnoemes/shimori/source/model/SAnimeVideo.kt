package com.gnoemes.shimori.source.model

data class SAnimeVideo(
    val id: Long = 0,
    val titleId: Long = 0,
    val name: String? = null,
    val url: String = "",
    val imageUrl: String? = null,
    val type: String,
    val hosting: String? = null
)
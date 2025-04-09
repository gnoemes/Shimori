package com.gnoemes.shimori.source.model

data class SCharacter(
    val id: Long = 0,
    val name: String = "",
    val nameRu: String? = null,
    val nameEn: String? = null,
    val image: SImage? = null,
    val url: String? = null,
    val description: String? = null,
    val descriptionSourceUrl: String? = null,
)
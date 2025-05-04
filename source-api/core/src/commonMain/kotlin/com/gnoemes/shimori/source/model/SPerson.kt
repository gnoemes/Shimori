package com.gnoemes.shimori.source.model

import kotlinx.datetime.LocalDate

data class SPerson(
    val id: Long = 0,
    val name: String = "",
    val nameRu: String? = null,
    val nameEn: String? = null,
    val image: SImage? = null,
    val url: String? = null,
    val isMangaka: Boolean = false,
    val isProducer: Boolean = false,
    val isSeyu: Boolean = false,
    val birthDate: LocalDate? = null,
    val deceasedDate: LocalDate? = null,
)
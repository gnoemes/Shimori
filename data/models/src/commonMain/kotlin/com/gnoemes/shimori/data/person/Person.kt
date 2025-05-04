package com.gnoemes.shimori.data.person

import com.gnoemes.shimori.data.ShimoriContentEntity
import com.gnoemes.shimori.data.common.ShimoriImage
import kotlinx.datetime.LocalDate

@kotlinx.serialization.Serializable
data class Person(
    override val id: Long = 0,
    override val name: String = "",
    override val nameRu: String? = null,
    override val nameEn: String? = null,
    override val image: ShimoriImage? = null,
    override val url: String? = null,
    val isMangaka: Boolean = false,
    val isProducer: Boolean = false,
    val isSeyu: Boolean = false,
    val birthDate: LocalDate? = null,
    val deceasedDate: LocalDate? = null,
) : ShimoriContentEntity
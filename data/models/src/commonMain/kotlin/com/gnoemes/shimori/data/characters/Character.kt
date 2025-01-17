package com.gnoemes.shimori.data.characters

import com.gnoemes.shimori.data.ShimoriContentEntity
import com.gnoemes.shimori.data.common.ShimoriImage

@kotlinx.serialization.Serializable
data class Character(
    override val id: Long = 0,
    override val name: String = "",
    override val nameRu: String? = null,
    override val nameEn: String? = null,
    override val image: ShimoriImage? = null,
    override val url: String? = null,
    val description: String? = null,
    val descriptionSourceUrl: String? = null,
) : ShimoriContentEntity
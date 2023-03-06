package com.gnoemes.shimori.data.core.entities.titles.anime

import com.gnoemes.shimori.data.core.entities.ShimoriEntity
import com.gnoemes.shimori.data.core.entities.common.ShimoriImage

@kotlinx.serialization.Serializable
data class AnimeScreenshot(
    override val id: Long = 0,
    val titleId: Long = 0,
    val image: ShimoriImage,
) : ShimoriEntity
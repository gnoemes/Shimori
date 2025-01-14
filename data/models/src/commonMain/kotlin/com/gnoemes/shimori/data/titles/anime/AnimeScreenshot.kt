package com.gnoemes.shimori.data.titles.anime

import com.gnoemes.shimori.data.ShimoriEntity
import com.gnoemes.shimori.data.common.ShimoriImage

@kotlinx.serialization.Serializable
data class AnimeScreenshot(
    override val id: Long = 0,
    val titleId: Long = 0,
    val image: ShimoriImage,
) : ShimoriEntity
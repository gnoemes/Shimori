package com.gnoemes.shimori.data.common

import com.gnoemes.shimori.data.ShimoriEntity

@kotlinx.serialization.Serializable
data class Studio(
    override val id: Long = 0,
    val sourceId: Long,
    val name: String = "",
    val imageUrl: String? = null,
) : ShimoriEntity
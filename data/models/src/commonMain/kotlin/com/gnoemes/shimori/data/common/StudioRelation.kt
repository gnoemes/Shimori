package com.gnoemes.shimori.data.common

import com.gnoemes.shimori.data.ShimoriEntity

@kotlinx.serialization.Serializable
data class StudioRelation(
    override val id: Long = 0,
    val studioId: Long,
    val sourceId: Long,
    val targetId: Long,
) : ShimoriEntity
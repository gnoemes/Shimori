package com.gnoemes.shimori.data.core.entities.app

import com.gnoemes.shimori.data.core.entities.ShimoriEntity

@kotlinx.serialization.Serializable
data class SourceIdsSync(
    override val id: Long = 0,
    val sourceId: Long,
    val localId: Long,
    val remoteId: Long,
    val type: SourceDataType
) : ShimoriEntity
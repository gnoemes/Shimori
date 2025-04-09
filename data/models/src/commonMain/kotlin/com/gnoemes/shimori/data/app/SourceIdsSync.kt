package com.gnoemes.shimori.data.app

import com.gnoemes.shimori.data.ShimoriEntity
import com.gnoemes.shimori.source.model.SourceDataType

@kotlinx.serialization.Serializable
data class SourceIdsSync(
    override val id: Long = 0,
    val sourceId: Long,
    val localId: Long,
    val remoteId: Long,
    val type: SourceDataType
) : ShimoriEntity
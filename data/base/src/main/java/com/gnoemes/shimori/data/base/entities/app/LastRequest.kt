package com.gnoemes.shimori.data.base.entities.app

import com.gnoemes.shimori.data.base.entities.ShimoriEntity
import kotlinx.datetime.Instant

@kotlinx.serialization.Serializable
data class LastRequest(
    override val id: Long,
    val request: Request,
    val entityId : Long,
    val timeStamp : Instant
) : ShimoriEntity
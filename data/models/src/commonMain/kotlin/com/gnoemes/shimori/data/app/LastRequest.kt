package com.gnoemes.shimori.data.app

import com.gnoemes.shimori.data.ShimoriEntity
import kotlinx.datetime.Instant

@kotlinx.serialization.Serializable
data class LastRequest(
    override val id: Long = 0,
    val request: Request,
    val entityId : Long,
    val timeStamp : Instant
) : ShimoriEntity
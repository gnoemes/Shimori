package com.gnoemes.shimori.data.core.entities.rate

import com.gnoemes.shimori.data.core.entities.ShikimoriEntity
import com.gnoemes.shimori.data.core.entities.ShimoriEntity
import kotlinx.datetime.Instant

@kotlinx.serialization.Serializable
data class Rate(
    override val id: Long = 0,
    override val shikimoriId: Long = 0,
    val targetId: Long,
    val targetType: RateTargetType,
    val targetShikimoriId: Long? = null,
    val status: RateStatus,
    val score: Int? = null,
    val comment: String? = null,
    val progress: Int = 0,
    val reCounter: Int = 0,
    val dateCreated: Instant? = null,
    val dateUpdated: Instant? = null
) : ShimoriEntity, ShikimoriEntity


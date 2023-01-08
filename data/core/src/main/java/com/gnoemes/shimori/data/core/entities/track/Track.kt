package com.gnoemes.shimori.data.core.entities.track

import com.gnoemes.shimori.data.core.entities.ShikimoriEntity
import com.gnoemes.shimori.data.core.entities.ShimoriEntity
import kotlinx.datetime.Instant

@kotlinx.serialization.Serializable
data class Track(
    override val id: Long = 0,
    override val shikimoriId: Long = 0,
    val targetId: Long,
    val targetType: TrackTargetType,
    val targetShikimoriId: Long? = null,
    val status: TrackStatus,
    val score: Int? = null,
    val comment: String? = null,
    val progress: Int = 0,
    val reCounter: Int = 0,
    val dateCreated: Instant? = null,
    val dateUpdated: Instant? = null
) : ShimoriEntity, ShikimoriEntity


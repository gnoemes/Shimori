package com.gnoemes.shimori.data.track

import com.gnoemes.shimori.data.ShimoriEntity
import kotlinx.datetime.Instant

@kotlinx.serialization.Serializable
data class Track(
    override val id: Long = 0,
    val targetId: Long,
    val targetType: TrackTargetType,
    val status: TrackStatus,
    val score: Int? = null,
    val comment: String? = null,
    val progress: Int = 0,
    val reCounter: Int = 0,
    val dateCreated: Instant? = null,
    val dateUpdated: Instant? = null
) : ShimoriEntity


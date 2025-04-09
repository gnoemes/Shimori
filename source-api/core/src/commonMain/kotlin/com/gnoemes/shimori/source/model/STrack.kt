package com.gnoemes.shimori.source.model

import kotlinx.datetime.Instant

data class STrack(
    val id: Long,
    val targetId: Long,
    val targetType: SourceDataType,
    val status: STrackStatus,
    val score: Int? = null,
    val comment: String? = null,
    val progress: Int = 0,
    val reCounter: Int = 0,
    val dateCreated: Instant? = null,
    val dateUpdated: Instant? = null
)
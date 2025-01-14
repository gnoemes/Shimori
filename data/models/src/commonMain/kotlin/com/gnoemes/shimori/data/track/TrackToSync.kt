package com.gnoemes.shimori.data.track

import com.gnoemes.shimori.data.ShimoriEntity
import com.gnoemes.shimori.data.app.SyncAction
import kotlinx.datetime.Instant

@kotlinx.serialization.Serializable
data class TrackToSync(
    override val id: Long = 0,
    val trackId: Long,
    val action: SyncAction,
    val attempts: Int = 0,
    val lastAttempt: Instant? = null,
    val attemptSourceId: Long? = null
) : ShimoriEntity
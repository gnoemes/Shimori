package com.gnoemes.shimori.data.core.entities.track

import com.gnoemes.shimori.data.core.entities.ShimoriEntity
import com.gnoemes.shimori.data.core.entities.app.SyncAction
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
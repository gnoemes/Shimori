package com.gnoemes.shimori.data.events

import com.benasher44.uuid.uuid4
import com.gnoemes.shimori.data.TitleWithTrackEntity
import com.gnoemes.shimori.data.common.ShimoriImage
import com.gnoemes.shimori.data.track.Track
import com.gnoemes.shimori.data.track.TrackStatus
import com.gnoemes.shimori.data.track.TrackTargetType

sealed class TrackUiEvents(
    val navigation: Boolean = false,
    val eventId: Long = uuid4().mostSignificantBits
) {
    data class IncrementerProgress(
        val title: TitleWithTrackEntity,
        val oldTrack: Track,
        val newProgress: Int
    ) : TrackUiEvents()

    data class PinStatusChanged(
        val title: TitleWithTrackEntity,
        val pinned: Boolean
    ) : TrackUiEvents()

    data class TrackDeleted(
        val image: ShimoriImage?,
        val track: Track
    ) : TrackUiEvents()

    data class OpenEdit(
        val targetId: Long,
        val targetType: TrackTargetType,
        val predefinedStatus: TrackStatus? = null
    ) : TrackUiEvents(navigation = true)
}
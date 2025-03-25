package com.gnoemes.shimori.data.track

import com.benasher44.uuid.uuid4
import com.gnoemes.shimori.data.TitleWithTrackEntity
import com.gnoemes.shimori.data.common.ShimoriImage

sealed class ListsUiEvents(
    val navigation: Boolean = false,
    val eventId: Long = uuid4().mostSignificantBits
) {
    data class IncrementerProgress(
        val title: TitleWithTrackEntity,
        val oldTrack: Track,
        val newProgress: Int
    ) : ListsUiEvents()

    data class PinStatusChanged(
        val title: TitleWithTrackEntity,
        val pinned: Boolean
    ) : ListsUiEvents()

    data class TrackDeleted(
        val image: ShimoriImage?,
        val track: Track
    ) : ListsUiEvents()

    data class OpenEdit(
        val targetId: Long,
        val targetType: TrackTargetType,
        val predefinedStatus: TrackStatus? = null
    ) : ListsUiEvents(navigation = true)
}
package com.gnoemes.shimori.data.lists

import com.gnoemes.shimori.data.TitleWithTrackEntity
import com.gnoemes.shimori.data.common.ShimoriImage
import com.gnoemes.shimori.data.track.Track
import com.gnoemes.shimori.data.track.TrackStatus
import com.gnoemes.shimori.data.track.TrackTargetType

sealed class ListsUiEvents {
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
    ) : ListsUiEvents()
}
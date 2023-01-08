package com.gnoemes.shimori.data.list

import com.gnoemes.shimori.data.core.entities.TitleWithTrackEntity
import com.gnoemes.shimori.data.core.entities.common.ShimoriImage
import com.gnoemes.shimori.data.core.entities.track.Track

sealed class ListsUiEvents {
    data class IncrementerProgress(
        val title: TitleWithTrackEntity,
        val oldTrack: Track,
        val newProgress: Int
    ) : ListsUiEvents()

    data class PinStatusChanged(
        val title: TitleWithTrackEntity,
        val pinned : Boolean
    ) : ListsUiEvents()

    data class TrackDeleted(
        val image: ShimoriImage?,
        val track: Track
    ) : ListsUiEvents()
}
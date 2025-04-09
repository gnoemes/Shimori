package com.gnoemes.shimori.data.source.mapper

import com.gnoemes.shimori.base.utils.TwoWayMapper
import com.gnoemes.shimori.data.track.TrackStatus
import com.gnoemes.shimori.source.model.STrackStatus
import me.tatarka.inject.annotations.Inject

@Inject
class SourceTrackStatusMapper : TwoWayMapper<STrackStatus, TrackStatus> {
    override fun map(from: STrackStatus): TrackStatus {
        return when (from) {
            STrackStatus.Planned -> TrackStatus.PLANNED
            STrackStatus.Watching -> TrackStatus.WATCHING
            STrackStatus.ReWatching -> TrackStatus.REWATCHING
            STrackStatus.Completed -> TrackStatus.COMPLETED
            STrackStatus.OnHold -> TrackStatus.ON_HOLD
            STrackStatus.Dropped -> TrackStatus.DROPPED
        }
    }

    override fun mapInverse(from: TrackStatus): STrackStatus {
        return when (from) {
            TrackStatus.PLANNED -> STrackStatus.Planned
            TrackStatus.WATCHING -> STrackStatus.Watching
            TrackStatus.REWATCHING -> STrackStatus.ReWatching
            TrackStatus.COMPLETED -> STrackStatus.Completed
            TrackStatus.ON_HOLD -> STrackStatus.OnHold
            TrackStatus.DROPPED -> STrackStatus.Dropped
        }
    }
}
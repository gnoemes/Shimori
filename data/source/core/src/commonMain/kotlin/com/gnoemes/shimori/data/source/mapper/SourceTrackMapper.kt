package com.gnoemes.shimori.data.source.mapper

import com.gnoemes.shimori.base.utils.TwoWayMapper
import com.gnoemes.shimori.data.track.Track
import com.gnoemes.shimori.data.track.TrackTargetType
import com.gnoemes.shimori.source.model.STrack
import com.gnoemes.shimori.source.model.SourceDataType
import me.tatarka.inject.annotations.Inject

@Inject
class SourceTrackMapper(
    private val statusMapper: SourceTrackStatusMapper
) : TwoWayMapper<STrack, Track> {

    override fun map(from: STrack): Track {
        val trackTargetType = when (from.targetType) {
            SourceDataType.Anime -> TrackTargetType.ANIME
            SourceDataType.Manga -> TrackTargetType.MANGA
            SourceDataType.Ranobe -> TrackTargetType.RANOBE
            else -> throw IllegalArgumentException("Unsupported target type: ${from.targetType}")
        }
        return Track(
            id = from.id,
            targetId = from.targetId,
            targetType = trackTargetType,
            status = statusMapper.map(from.status),
            score = from.score,
            comment = from.comment,
            progress = from.progress,
            reCounter = from.reCounter,
            dateCreated = from.dateCreated,
            dateUpdated = from.dateUpdated
        )
    }


    override fun mapInverse(from: Track): STrack {
        return STrack(
            id = from.id,
            targetId = from.targetId,
            targetType = from.targetType.sourceDataType,
            status = statusMapper.mapInverse(from.status),
            score = from.score,
            comment = from.comment,
            progress = from.progress,
            reCounter = from.reCounter,
            dateCreated = from.dateCreated,
            dateUpdated = from.dateUpdated
        )
    }
}
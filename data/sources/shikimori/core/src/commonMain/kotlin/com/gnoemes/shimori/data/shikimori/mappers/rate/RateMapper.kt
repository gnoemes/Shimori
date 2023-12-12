package com.gnoemes.shimori.data.shikimori.mappers.rate

import com.gnoemes.shimori.base.utils.TwoWayMapper
import com.gnoemes.shimori.data.shikimori.models.rates.UserRateResponse
import com.gnoemes.shimori.data.track.Track
import com.gnoemes.shimori.data.track.TrackTargetType
import me.tatarka.inject.annotations.Inject

@Inject
class RateMapper constructor(
    private val typeMapper: RateTargetTypeMapper,
    private val statusMapper: RateStatusMapper
) : TwoWayMapper<UserRateResponse, Track> {

    override fun map(from: UserRateResponse): Track {
        val targetType = typeMapper.map(from.targetType)
        val status = statusMapper.map(from.status)

        requireNotNull(targetType) { "Rate#${from.id} targetType is null" }
        requireNotNull(status) { "Rate#${from.id} status is null" }

        val progress = (when (targetType) {
            TrackTargetType.ANIME -> from.episodes
            TrackTargetType.MANGA, TrackTargetType.RANOBE -> from.chapters
            else -> null
        }) ?: 0

        return Track(
            id = from.id,
            targetId = from.targetId,
            targetType = targetType,
            score = from.score?.toInt(),
            status = status,
            dateCreated = from.dateCreated,
            dateUpdated = from.dateUpdated,
            progress = progress,
            reCounter = from.rewatches ?: 0,
            comment = from.text
        )
    }

    override fun mapInverse(from: Track): UserRateResponse {
        val targetType = typeMapper.mapInverse(from.targetType)
        val status = statusMapper.mapInverse(from.status)

        requireNotNull(targetType) { "Rate#${from.id} targetType is null" }
        requireNotNull(status) { "Rate#${from.id} status is null" }

        val episodes = if (from.targetType.anime) from.progress else null
        val chapters = if (from.targetType.anime.not()) from.progress else null

        return UserRateResponse(
            id = from.id,
            targetId = from.targetId,
            targetType = targetType,
            score = from.score?.toDouble(),
            status = status,
            dateCreated = from.dateCreated,
            dateUpdated = from.dateUpdated,
            episodes = episodes,
            chapters = chapters,
            volumes = null,
            rewatches = from.reCounter,
            text = from.comment
        )
    }
}
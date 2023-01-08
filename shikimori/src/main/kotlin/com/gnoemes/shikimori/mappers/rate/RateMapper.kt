package com.gnoemes.shikimori.mappers.rate

import com.gnoemes.shikimori.entities.rates.UserRateResponse
import com.gnoemes.shimori.data.core.entities.track.Track
import com.gnoemes.shimori.data.core.entities.track.TrackTargetType
import com.gnoemes.shimori.data.core.mappers.TwoWayMapper

internal class RateMapper constructor(
    private val typeMapper: RateTargetTypeMapper,
    private val statusMapper: RateStatusMapper
) : TwoWayMapper<UserRateResponse, Track> {

    override suspend fun map(from: UserRateResponse): Track {
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
            shikimoriId = from.id,
            targetId = 0,
            targetType = targetType,
            targetShikimoriId = from.targetId,
            score = from.score?.toInt(),
            status = status,
            dateCreated = from.dateCreated,
            dateUpdated = from.dateUpdated,
            progress = progress,
            reCounter = from.rewatches ?: 0,
            comment = from.text
        )
    }

    override suspend fun mapInverse(from: Track): UserRateResponse {
        val targetType = typeMapper.mapInverse(from.targetType)
        val status = statusMapper.mapInverse(from.status)
        val targetShikimoriId = from.targetShikimoriId

        requireNotNull(targetType) { "Rate#${from.shikimoriId} targetType is null" }
        requireNotNull(status) { "Rate#${from.shikimoriId} status is null" }
        requireNotNull(targetShikimoriId) { "Rate#${from.shikimoriId} target id is null" }

        val episodes = if (from.targetType.anime) from.progress else null
        val chapters = if (from.targetType.anime.not()) from.progress else null

        return UserRateResponse(
            id = from.shikimoriId,
            targetId = targetShikimoriId,
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
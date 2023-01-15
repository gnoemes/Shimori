package com.gnoemes.shikimori.mappers.rate

import com.gnoemes.shikimori.entities.rates.RateResponse
import com.gnoemes.shikimori.mappers.ranobe.RanobeTypeMapper
import com.gnoemes.shimori.data.core.entities.track.Track
import com.gnoemes.shimori.data.core.entities.track.TrackTargetType
import com.gnoemes.shimori.data.core.mappers.Mapper

internal class RateResponseToRateMapper(
    private val statusMapper: RateStatusMapper,
    private val ranobeTypeMapper: RanobeTypeMapper,
) : Mapper<Pair<RateResponse?, TrackTargetType?>, Track?> {

    override suspend fun map(pair: Pair<RateResponse?, TrackTargetType?>): Track? {
        val from = pair.first ?: return null

        val targetType = when {
            from.anime != null -> TrackTargetType.ANIME
            from.manga != null && ranobeTypeMapper.map(from.manga.type) != null -> TrackTargetType.RANOBE
            from.manga != null -> TrackTargetType.MANGA
            else -> pair.second
        }
        val status = statusMapper.map(from.status)

        requireNotNull(targetType) { "Rate#${from.id} targetType is null" }
        requireNotNull(status) { "Rate#${from.id} status is null" }

        val progress = (when (targetType) {
            TrackTargetType.ANIME -> from.episodes
            TrackTargetType.MANGA, TrackTargetType.RANOBE -> from.chapters
            else -> null
        }) ?: 0

        val targetShikimoriId = when(targetType) {
            TrackTargetType.ANIME -> from.anime?.id
            else -> from.manga?.id
        }

        requireNotNull(targetShikimoriId) { "Rate#${from.id} target id is null" }

        return Track(
            id = from.id,
            targetId = targetShikimoriId,
            targetType = targetType,
            score = from.score,
            status = status,
            dateCreated = from.dateCreated,
            dateUpdated = from.dateUpdated,
            progress = progress,
            reCounter = from.rewatches ?: 0,
            comment = from.text
        )
    }
}
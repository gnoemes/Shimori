package com.gnoemes.shikimori.mappers.rate

import com.gnoemes.shikimori.entities.rates.RateResponse
import com.gnoemes.shikimori.mappers.ranobe.RanobeTypeMapper
import com.gnoemes.shimori.data.core.entities.rate.Rate
import com.gnoemes.shimori.data.core.entities.rate.RateTargetType
import com.gnoemes.shimori.data.core.mappers.Mapper

internal class RateResponseToRateMapper(
    private val statusMapper: RateStatusMapper,
    private val ranobeTypeMapper: RanobeTypeMapper,
) : Mapper<Pair<RateResponse?, RateTargetType?>, Rate?> {

    override suspend fun map(pair: Pair<RateResponse?, RateTargetType?>): Rate? {
        val from = pair.first ?: return null

        val targetType = when {
            from.anime != null -> RateTargetType.ANIME
            from.manga != null && ranobeTypeMapper.map(from.manga.type) != null -> RateTargetType.RANOBE
            from.manga != null -> RateTargetType.MANGA
            else -> pair.second
        }
        val status = statusMapper.map(from.status)

        requireNotNull(targetType) { "Rate#${from.id} targetType is null" }
        requireNotNull(status) { "Rate#${from.id} status is null" }

        val progress = (when (targetType) {
            RateTargetType.ANIME -> from.episodes
            RateTargetType.MANGA, RateTargetType.RANOBE -> from.chapters
            else -> null
        }) ?: 0

        val targetShikimoriId = when(targetType) {
            RateTargetType.ANIME -> from.anime?.id
            else -> from.manga?.id
        }

        return Rate(
            shikimoriId = from.id,
            //init later
            targetId = 0,
            targetType = targetType,
            targetShikimoriId = targetShikimoriId,
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
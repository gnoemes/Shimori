package com.gnoemes.shikimori.mappers.rate

import com.gnoemes.shikimori.entities.rates.UserRateResponse
import com.gnoemes.shimori.data.base.entities.rate.Rate
import com.gnoemes.shimori.data.base.entities.rate.RateTargetType
import com.gnoemes.shimori.data.base.mappers.TwoWayMapper

internal class RateMapper constructor(
    private val typeMapper: RateTargetTypeMapper,
    private val statusMapper: RateStatusMapper
) : TwoWayMapper<UserRateResponse, Rate> {

    override suspend fun map(from: UserRateResponse): Rate {
        val targetType = typeMapper.map(from.targetType)
        val status = statusMapper.map(from.status)

        requireNotNull(targetType) { "Rate#${from.id} targetType is null" }
        requireNotNull(status) { "Rate#${from.id} status is null" }

        val progress = (when (targetType) {
            RateTargetType.ANIME -> from.episodes
            RateTargetType.MANGA, RateTargetType.RANOBE -> from.chapters
            else -> null
        }) ?: 0

        return Rate(
            shikimoriId = from.id,
            targetId = from.targetId,
            score = from.score?.toInt(),
            targetType = targetType,
            status = status,
            dateCreated = from.dateCreated,
            dateUpdated = from.dateUpdated,
            progress = progress,
            reCounter = from.rewatches ?: 0,
            comment = from.text
        )
    }

    override suspend fun mapInverse(from: Rate): UserRateResponse {
        val targetType = typeMapper.mapInverse(from.targetType)
        val status = statusMapper.mapInverse(from.status)

        requireNotNull(targetType) { "Rate#${from.shikimoriId} targetType is null" }
        requireNotNull(status) { "Rate#${from.shikimoriId} status is null" }

        val episodes = if (from.targetType.anime) from.progress else null
        val chapters = if (from.targetType.anime.not()) from.progress else null

        return UserRateResponse(
            id = from.shikimoriId,
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
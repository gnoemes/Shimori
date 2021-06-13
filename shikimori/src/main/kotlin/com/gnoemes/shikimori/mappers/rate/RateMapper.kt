package com.gnoemes.shikimori.mappers.rate

import com.gnoemes.shikimori.entities.common.ShikimoriContentType
import com.gnoemes.shikimori.entities.rates.UserRateResponse
import com.gnoemes.shimori.data_base.mappers.TwoWayMapper
import com.gnoemes.shimori.model.rate.Rate
import com.gnoemes.shimori.model.rate.RateTargetType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class RateMapper @Inject constructor(
    private val typeMapper: RateTargetTypeMapper,
    private val statusMapper: RateStatusMapper
) : TwoWayMapper<UserRateResponse, Rate> {

    override suspend fun map(from: UserRateResponse): Rate {
        val targetType = typeMapper.map(from.targetType)

        val animeId = if (targetType == RateTargetType.ANIME) from.targetId else null
        val mangaId = if (targetType == RateTargetType.MANGA) from.targetId else null
        val ranobeId = if (targetType == RateTargetType.RANOBE) from.targetId else null

        return Rate(
                shikimoriId = from.id,
                animeId = animeId,
                mangaId = mangaId,
                ranobeId = ranobeId,
                score = from.score?.toInt(),
                targetType = targetType,
                status = statusMapper.map(from.status),
                dateCreated = from.dateCreated,
                dateUpdated = from.dateUpdated,
                episodes = from.episodes,
                chapters = from.chapters,
                volumes = from.volumes,
                reCounter = from.rewatches,
                comment = from.text
        )
    }

    override suspend fun mapInverse(from: Rate): UserRateResponse {
        val targetType = typeMapper.mapInverse(from.targetType)

        val targetId = when (targetType) {
            ShikimoriContentType.ANIME -> from.animeId
            ShikimoriContentType.MANGA -> from.mangaId
            ShikimoriContentType.RANOBE -> from.ranobeId
            else -> null
        }

        return UserRateResponse(
                id = from.shikimoriId,
                targetId = targetId,
                targetType = targetType,
                score = from.score?.toDouble(),
                status = statusMapper.mapInverse(from.status),
                dateCreated = from.dateCreated,
                dateUpdated = from.dateUpdated,
                episodes = from.episodes,
                chapters = from.chapters,
                volumes = from.volumes,
                rewatches = from.reCounter,
                text = from.comment
        )
    }
}
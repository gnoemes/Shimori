package com.gnoemes.shimori.data.shared

import com.gnoemes.shimori.data.base.entities.rate.Rate
import com.gnoemes.shimori.data.base.mappers.TwoWayMapper

typealias RateDAO = comgnoemesshimoridatadb.Rate

internal object RateMapper : TwoWayMapper<RateDAO?, Rate?> {
    override suspend fun map(from: RateDAO?): Rate? {
        if (from == null) return null

        return Rate(
            id = from.id,
            shikimoriId = from.shikimori_id ?: 0,
            targetId = from.target_id,
            targetType = from.target_type,
            status = from.status,
            score = from.score,
            comment = from.comment,
            progress = from.progress,
            reCounter = from.re_counter,
            dateCreated = from.date_created,
            dateUpdated = from.date_updated,
        )
    }

    override suspend fun mapInverse(from: Rate?): RateDAO? {
        if (from == null) return null

        return RateDAO(
            id = from.id,
            shikimori_id = from.shikimoriId,
            target_id = from.targetId,
            target_type = from.targetType,
            status = from.status,
            score = from.score,
            comment = from.comment,
            progress = from.progress,
            re_counter = from.reCounter,
            date_created = from.dateCreated,
            date_updated = from.dateUpdated,
        )
    }
}
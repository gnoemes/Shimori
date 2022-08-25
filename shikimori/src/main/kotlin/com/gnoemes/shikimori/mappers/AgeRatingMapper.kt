package com.gnoemes.shikimori.mappers

import com.gnoemes.shimori.data.core.entities.common.AgeRating
import com.gnoemes.shimori.data.core.mappers.Mapper
import com.gnoemes.shikimori.entities.common.AgeRating as ShikimoriAgeRating

internal class AgeRatingMapper : Mapper<ShikimoriAgeRating?, AgeRating> {
    override suspend fun map(from: ShikimoriAgeRating?): AgeRating = when (from) {
        ShikimoriAgeRating.G -> AgeRating.G
        ShikimoriAgeRating.PG -> AgeRating.PG
        ShikimoriAgeRating.PG_13 -> AgeRating.PG_13
        ShikimoriAgeRating.R -> AgeRating.R
        ShikimoriAgeRating.R_PLUS -> AgeRating.R_PLUS
        ShikimoriAgeRating.RX -> AgeRating.RX
        else -> AgeRating.NONE
    }
}
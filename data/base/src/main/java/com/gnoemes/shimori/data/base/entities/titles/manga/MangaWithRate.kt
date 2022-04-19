package com.gnoemes.shimori.data.base.entities.titles.manga

import com.gnoemes.shimori.data.base.entities.TitleWithRate
import com.gnoemes.shimori.data.base.entities.rate.Rate
import com.gnoemes.shimori.data.base.entities.rate.RateTargetType

@kotlinx.serialization.Serializable
data class MangaWithRate(
    override val entity: Manga,
    override val rate: Rate?
) : TitleWithRate<Manga> {
    override val id: Long = entity.id
    override val type: RateTargetType = entity.type
}
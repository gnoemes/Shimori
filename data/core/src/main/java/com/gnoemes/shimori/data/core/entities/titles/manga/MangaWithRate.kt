package com.gnoemes.shimori.data.core.entities.titles.manga

import com.gnoemes.shimori.data.core.entities.PaginatedEntity
import com.gnoemes.shimori.data.core.entities.TitleWithRate
import com.gnoemes.shimori.data.core.entities.rate.Rate
import com.gnoemes.shimori.data.core.entities.rate.RateTargetType

@kotlinx.serialization.Serializable
data class MangaWithRate(
    override val entity: Manga,
    override val rate: Rate?
) : TitleWithRate<Manga>, PaginatedEntity {
    override val id: Long = entity.id
    override val type: RateTargetType = entity.type
}
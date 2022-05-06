package com.gnoemes.shimori.data.core.entities.titles.anime

import com.gnoemes.shimori.data.core.entities.TitleWithRate
import com.gnoemes.shimori.data.core.entities.rate.Rate
import com.gnoemes.shimori.data.core.entities.rate.RateTargetType

@kotlinx.serialization.Serializable
data class AnimeWithRate(
    override val entity : Anime,
    override val rate: Rate?
) : TitleWithRate<Anime> {
    override val id: Long = entity.id
    override val type: RateTargetType = entity.type
}
package com.gnoemes.shimori.data.base.entities.titles.anime

import com.gnoemes.shimori.data.base.entities.TitleWithRate
import com.gnoemes.shimori.data.base.entities.rate.Rate
import com.gnoemes.shimori.data.base.entities.rate.RateTargetType

@kotlinx.serialization.Serializable
data class AnimeWithRate(
    override val entity : Anime,
    override val rate: Rate?
) : TitleWithRate<Anime> {
    override val id: Long = entity.id
    override val type: RateTargetType = entity.type
}
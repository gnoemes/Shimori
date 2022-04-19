package com.gnoemes.shimori.data.base.entities.titles.ranobe

import com.gnoemes.shimori.data.base.entities.TitleWithRate
import com.gnoemes.shimori.data.base.entities.rate.Rate
import com.gnoemes.shimori.data.base.entities.rate.RateTargetType
import com.gnoemes.shimori.data.base.entities.titles.manga.Manga

@kotlinx.serialization.Serializable
data class RanobeWithRate(
    override val entity: Ranobe,
    override val rate: Rate?
) : TitleWithRate<Ranobe> {
    override val id: Long = entity.id
    override val type: RateTargetType = entity.type
}
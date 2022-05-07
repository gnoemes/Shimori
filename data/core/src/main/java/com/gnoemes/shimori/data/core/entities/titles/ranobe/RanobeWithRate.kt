package com.gnoemes.shimori.data.core.entities.titles.ranobe

import com.gnoemes.shimori.data.core.entities.PaginatedEntity
import com.gnoemes.shimori.data.core.entities.TitleWithRate
import com.gnoemes.shimori.data.core.entities.rate.Rate
import com.gnoemes.shimori.data.core.entities.rate.RateTargetType

@kotlinx.serialization.Serializable
data class RanobeWithRate(
    override val entity: Ranobe,
    override val rate: Rate?
) : TitleWithRate<Ranobe>, PaginatedEntity {
    override val id: Long = entity.id
    override val type: RateTargetType = entity.type
}
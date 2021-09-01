package com.gnoemes.shimori.model.ranobe

import androidx.room.Embedded
import androidx.room.Relation
import com.gnoemes.shimori.model.EntityWithRate
import com.gnoemes.shimori.model.rate.Rate

class RanobeWithRate : EntityWithRate<Ranobe> {
    @Embedded
    override lateinit var entity: Ranobe

    @Relation(parentColumn = "ranobe_shikimori_id", entityColumn = "ranobe_id")
    override var relations: List<Rate>? = null

    override val rate: Rate?
        get() = relations?.firstOrNull()

    override fun equals(other: Any?): Boolean = when {
        other === this -> true
        other is RanobeWithRate -> entity == other.entity && rate == other.rate
        else -> false
    }

    override fun hashCode(): Int = arrayOf(entity, rate).contentHashCode()

    val id get() = entity.id
}
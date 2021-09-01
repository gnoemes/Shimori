package com.gnoemes.shimori.model.anime

import androidx.room.Embedded
import androidx.room.Relation
import com.gnoemes.shimori.model.EntityWithRate
import com.gnoemes.shimori.model.rate.Rate

class AnimeWithRate : EntityWithRate<Anime> {
    @Embedded
    override lateinit var entity: Anime

    @Relation(parentColumn = "anime_shikimori_id", entityColumn = "anime_id")
    override var relations: List<Rate>? = null

    override val rate: Rate?
        get() = relations?.firstOrNull()

    override fun equals(other: Any?): Boolean = when {
        other === this -> true
        other is AnimeWithRate -> entity == other.entity && rate == other.rate
        else -> false
    }

    override fun hashCode(): Int = arrayOf(entity, rate).contentHashCode()

    val id get() = entity.id
}
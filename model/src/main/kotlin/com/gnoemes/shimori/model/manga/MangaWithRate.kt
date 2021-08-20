package com.gnoemes.shimori.model.manga

import androidx.room.Embedded
import androidx.room.Relation
import com.gnoemes.shimori.model.EntityWithRate
import com.gnoemes.shimori.model.rate.Rate

class MangaWithRate : EntityWithRate<Manga> {
    @Embedded
    override lateinit var entity: Manga

    @Relation(parentColumn = "manga_shikimori_id", entityColumn = "manga_id")
    override var relations: List<Rate>? = null

    override val rate: Rate?
        get() = relations?.firstOrNull()

    override fun equals(other: Any?): Boolean = when {
        other === this -> true
        other is MangaWithRate -> entity == other.entity && rate == other.rate
        else -> false
    }

    override fun hashCode(): Int = arrayOf(entity, rate).contentHashCode()
}
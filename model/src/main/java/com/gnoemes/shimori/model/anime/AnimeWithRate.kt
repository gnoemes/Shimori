package com.gnoemes.shimori.model.anime

import androidx.room.Embedded
import androidx.room.Relation
import com.gnoemes.shimori.model.rate.Rate

class AnimeWithRate {
    @Embedded
    var anime: Anime? = null

    @Relation(parentColumn = "rate_id", entityColumn = "shikimori_id")
    var rate: Rate? = null

    override fun equals(other: Any?): Boolean = when {
        other === this -> true
        other is AnimeWithRate -> anime == other.anime && rate == other.rate
        else -> false
    }

    override fun hashCode(): Int = arrayOf(anime, rate).contentHashCode()
}
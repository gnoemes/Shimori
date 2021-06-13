package com.gnoemes.common.textcreators

import android.content.Context
import com.gnoemes.shimori.model.ShimoriEntity
import com.gnoemes.shimori.model.anime.Anime
import com.gnoemes.shimori.model.rate.Rate
import javax.inject.Inject

class CompoundTextCreator @Inject constructor(
    private val context: Context,
    private val animeTextCreator: AnimeTextCreator
) {

    fun title(entity: ShimoriEntity) = when (entity) {
        is Anime -> animeTextCreator.title(entity)
        else -> throw IllegalArgumentException("$entity entity is not supported")
    }

    fun rateDescription(entity: ShimoriEntity) = when (entity) {
        is Anime -> animeTextCreator.rateDescription(entity)
        else -> throw IllegalArgumentException("$entity entity is not supported")
    }

    fun rateProgress(rate: Rate, entity: ShimoriEntity) = when(entity) {
        is Anime -> animeTextCreator.rateProgress(rate, entity)
        else -> throw IllegalArgumentException("$entity entity is not supported")
    }

}
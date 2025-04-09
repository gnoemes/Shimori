package com.gnoemes.shimori.source.shikimori.mappers.anime

import com.gnoemes.shimori.base.utils.Mapper
import com.gnoemes.shimori.source.shikimori.models.anime.ShikimoriAnimeType
import me.tatarka.inject.annotations.Inject

@Inject
class AnimeTypeMapper : Mapper<ShikimoriAnimeType?, String?> {

    override fun map(from: ShikimoriAnimeType?) = from?.name?.lowercase()
}
package com.gnoemes.shikimori.mappers.anime

import com.gnoemes.shikimori.entities.anime.ShikimoriAnimeVideoType
import com.gnoemes.shimori.data.core.entities.titles.anime.AnimeVideoType
import com.gnoemes.shimori.data.core.mappers.Mapper

internal class AnimeVideoTypeMapper : Mapper<ShikimoriAnimeVideoType?, AnimeVideoType> {

    override suspend fun map(from: ShikimoriAnimeVideoType?) = when (from) {
        is ShikimoriAnimeVideoType.Opening -> AnimeVideoType.Opening
        is ShikimoriAnimeVideoType.Ending -> AnimeVideoType.Ending
        is ShikimoriAnimeVideoType.Promo -> AnimeVideoType.Promo
        is ShikimoriAnimeVideoType.Commercial -> AnimeVideoType.Commercial
        else -> AnimeVideoType.Other
    }
}
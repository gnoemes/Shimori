package com.gnoemes.shimori.sources.shikimori.mappers.anime

import com.gnoemes.shimori.base.utils.Mapper
import com.gnoemes.shimori.data.titles.anime.AnimeType
import com.gnoemes.shimori.sources.shikimori.models.anime.ShikimoriAnimeType
import me.tatarka.inject.annotations.Inject

@Inject
class AnimeTypeMapper : Mapper<ShikimoriAnimeType?, AnimeType?> {

    override fun map(from: ShikimoriAnimeType?) = when (from) {
        ShikimoriAnimeType.TV -> AnimeType.Tv
        ShikimoriAnimeType.MOVIE -> AnimeType.Movie
        ShikimoriAnimeType.SPECIAL -> AnimeType.Special
        ShikimoriAnimeType.MUSIC -> AnimeType.Music
        ShikimoriAnimeType.OVA -> AnimeType.OVA
        ShikimoriAnimeType.ONA -> AnimeType.ONA
        ShikimoriAnimeType.TV_13 -> AnimeType.Tv_13
        ShikimoriAnimeType.TV_24 -> AnimeType.Tv_24
        ShikimoriAnimeType.TV_48 -> AnimeType.Tv_48
        else -> null
    }
}
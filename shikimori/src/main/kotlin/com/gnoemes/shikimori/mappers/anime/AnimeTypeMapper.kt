package com.gnoemes.shikimori.mappers.anime

import com.gnoemes.shikimori.entities.anime.ShikimoriAnimeType
import com.gnoemes.shimori.data.core.entities.titles.anime.AnimeType
import com.gnoemes.shimori.data.core.mappers.Mapper

internal class AnimeTypeMapper : Mapper<ShikimoriAnimeType?, AnimeType?> {

    override suspend fun map(from: ShikimoriAnimeType?) = when (from) {
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
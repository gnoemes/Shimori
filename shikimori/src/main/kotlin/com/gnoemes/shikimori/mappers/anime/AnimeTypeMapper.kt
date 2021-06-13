package com.gnoemes.shikimori.mappers.anime

import com.gnoemes.shikimori.entities.anime.ShikimoriAnimeType
import com.gnoemes.shimori.data_base.mappers.Mapper
import com.gnoemes.shimori.model.anime.AnimeType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class AnimeTypeMapper @Inject constructor(): Mapper<ShikimoriAnimeType?, AnimeType?> {

    override suspend fun map(from: ShikimoriAnimeType?) = when(from) {
        ShikimoriAnimeType.TV -> AnimeType.TV
        ShikimoriAnimeType.MOVIE -> AnimeType.MOVIE
        ShikimoriAnimeType.SPECIAL -> AnimeType.SPECIAL
        ShikimoriAnimeType.MUSIC -> AnimeType.MUSIC
        ShikimoriAnimeType.OVA -> AnimeType.OVA
        ShikimoriAnimeType.ONA -> AnimeType.ONA
        ShikimoriAnimeType.TV_13 -> AnimeType.TV_13
        ShikimoriAnimeType.TV_24 -> AnimeType.TV_24
        ShikimoriAnimeType.TV_48 -> AnimeType.TV_48
        else -> null
    }
}
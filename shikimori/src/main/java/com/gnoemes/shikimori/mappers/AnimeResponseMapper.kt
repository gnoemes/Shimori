package com.gnoemes.shikimori.mappers

import com.gnoemes.shikimori.entities.anime.AnimeResponse
import com.gnoemes.shimori.data_base.mappers.Mapper
import com.gnoemes.shimori.model.anime.Anime
import com.gnoemes.shimori.model.anime.AnimeType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class AnimeResponseMapper @Inject constructor(
    private val imageMapper: ImageResponseMapper
) : Mapper<AnimeResponse, Anime> {

    //TODO domain mapping
    override suspend fun map(from: AnimeResponse): Anime = Anime(
            from.id,
            from.name.trim(),
            from.nameRu?.trim(),
            imageMapper.map(from.image),
            from.url,

            AnimeType.MOVIE,
            from.score,
            from.status,
            from.episodes,
            from.episodesAired,
            from.dateAired,
            from.dateReleased
    )

}
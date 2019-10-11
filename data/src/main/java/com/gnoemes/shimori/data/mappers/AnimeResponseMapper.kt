package com.gnoemes.shimori.data.mappers

import com.gnoemes.shimori.data.entities.network.AnimeResponse
import com.gnoemes.shimori.model.anime.Anime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnimeResponseMapper @Inject constructor(
    private val imageMapper: ImageResponseMapper
) : Mapper<AnimeResponse, Anime> {

    override suspend fun map(from: AnimeResponse): Anime = Anime(
            from.id,
            from.name.trim(),
            from.nameRu?.trim(),
            imageMapper.map(from.image),
            from.url,
            from.type,
            from.score,
            from.status,
            from.episodes,
            from.episodesAired,
            from.dateAired,
            from.dateReleased
    )

}
package com.gnoemes.shikimori.mappers.anime

import com.gnoemes.shikimori.entities.anime.AnimeVideoResponse
import com.gnoemes.shimori.data.core.entities.titles.anime.AnimeVideo
import com.gnoemes.shimori.data.core.mappers.Mapper

internal class AnimeVideoMapper constructor(
    private val typeMapper: AnimeVideoTypeMapper,
) : Mapper<AnimeVideoResponse, AnimeVideo> {

    override suspend fun map(from: AnimeVideoResponse): AnimeVideo {
        return AnimeVideo(
            id = 0,
            name = from.name,
            url = from.url,
            imageUrl = from.imageUrl,
            type = typeMapper.map(from.type),
            hosting = from.hosting
        )
    }
}
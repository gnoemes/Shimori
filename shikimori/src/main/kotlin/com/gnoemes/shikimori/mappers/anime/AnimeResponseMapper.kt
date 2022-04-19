package com.gnoemes.shikimori.mappers.anime

import com.gnoemes.shikimori.SHIKIMORI_BASE_URL
import com.gnoemes.shikimori.appendHostIfNeed
import com.gnoemes.shikimori.entities.anime.AnimeResponse
import com.gnoemes.shikimori.mappers.ImageResponseMapper
import com.gnoemes.shikimori.mappers.TitleStatusMapper
import com.gnoemes.shimori.data.base.entities.titles.anime.Anime
import com.gnoemes.shimori.data.base.mappers.Mapper

internal class AnimeResponseMapper constructor(
    private val imageMapper: ImageResponseMapper,
    private val typeMapper: AnimeTypeMapper,
    private val titleStatusMapper: TitleStatusMapper,
) : Mapper<AnimeResponse, Anime> {

    override suspend fun map(from: AnimeResponse) = Anime(
        shikimoriId = from.id,
        name = from.name.trim(),
        nameRu = from.nameRu?.trim()?.ifEmpty { null },
        image = imageMapper.map(from.image),
        url = from.url.appendHostIfNeed(),
        anime_type = typeMapper.map(from.type)?.type,
        rating = from.score,
        status = titleStatusMapper.map(from.status),
        episodes = from.episodes,
        episodesAired = from.episodesAired,
        dateAired = from.dateAired,
        dateReleased = from.dateReleased
    )
}
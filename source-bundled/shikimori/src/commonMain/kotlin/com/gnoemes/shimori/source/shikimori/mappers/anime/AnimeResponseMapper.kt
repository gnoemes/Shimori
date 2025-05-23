package com.gnoemes.shimori.source.shikimori.mappers.anime

import com.gnoemes.shimori.base.utils.Mapper
import com.gnoemes.shimori.source.model.SAnime
import com.gnoemes.shimori.source.shikimori.Shikimori.Companion.appendHostIfNeed
import com.gnoemes.shimori.source.shikimori.ShikimoriValues
import com.gnoemes.shimori.source.shikimori.mappers.ImageResponseMapper
import com.gnoemes.shimori.source.shikimori.mappers.TitleStatusMapper
import com.gnoemes.shimori.source.shikimori.models.anime.AnimeResponse
import me.tatarka.inject.annotations.Inject

@Inject
class AnimeResponseMapper(
    private val imageMapper: ImageResponseMapper,
    private val typeMapper: AnimeTypeMapper,
    private val titleStatusMapper: TitleStatusMapper,
    private val values: ShikimoriValues,
) : Mapper<AnimeResponse, SAnime> {

    override fun map(from: AnimeResponse) = SAnime(
        id = from.id,
        name = from.name.trim(),
        nameRu = from.nameRu?.trim()?.ifEmpty { null },
        image = imageMapper.map(from.image),
        url = from.url.appendHostIfNeed(values),
        animeType = typeMapper.map(from.type),
        rating = from.score,
        status = titleStatusMapper.map(from.status),
        episodes = from.episodes,
        episodesAired = from.episodesAired,
        dateAired = from.dateAired,
        dateReleased = from.dateReleased
    )
}
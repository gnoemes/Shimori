package com.gnoemes.shikimori.mappers.anime

import com.gnoemes.shikimori.entities.anime.AnimeResponse
import com.gnoemes.shikimori.mappers.ImageResponseMapper
import com.gnoemes.shimori.data_base.mappers.Mapper
import com.gnoemes.shimori.model.ShimoriConstants
import com.gnoemes.shimori.model.anime.Anime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class AnimeResponseMapper @Inject constructor(
    private val imageMapper: ImageResponseMapper,
    private val typeMapper: AnimeTypeMapper
) : Mapper<AnimeResponse, Anime> {

    override suspend fun map(from: AnimeResponse) = Anime(
            shikimoriId = from.id,
            name = from.name.trim(),
            nameRu = from.nameRu?.trim(),
            image = imageMapper.map(from.image),
            url = from.url.appendHostIfNeed(),
            type = typeMapper.map(from.type),
            score = from.score,
            status = from.status,
            episodes = from.episodes,
            episodesAired = from.episodesAired,
            dateAired = from.dateAired,
            dateReleased = from.dateReleased
    )

    private fun String.appendHostIfNeed(host: String = ShimoriConstants.ShikimoriBaseUrl): String {
        return if (this.contains("http")) this else host + this
    }
}
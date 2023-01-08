package com.gnoemes.shikimori.mappers.anime

import com.gnoemes.shikimori.appendHostIfNeed
import com.gnoemes.shikimori.entities.anime.AnimeDetailsResponse
import com.gnoemes.shikimori.mappers.AgeRatingMapper
import com.gnoemes.shikimori.mappers.GenreMapper
import com.gnoemes.shikimori.mappers.ImageResponseMapper
import com.gnoemes.shikimori.mappers.TitleStatusMapper
import com.gnoemes.shikimori.mappers.rate.RateResponseToRateMapper
import com.gnoemes.shimori.data.core.entities.titles.anime.Anime
import com.gnoemes.shimori.data.core.entities.titles.anime.AnimeWithTrack
import com.gnoemes.shimori.data.core.entities.track.TrackTargetType
import com.gnoemes.shimori.data.core.mappers.Mapper

internal class AnimeDetailsMapper(
    private val imageMapper: ImageResponseMapper,
    private val typeMapper: AnimeTypeMapper,
    private val titleStatusMapper: TitleStatusMapper,
    private val rateMapper: RateResponseToRateMapper,
    private val ageRatingMapper: AgeRatingMapper,
    private val genreMapper: GenreMapper
) : Mapper<AnimeDetailsResponse, AnimeWithTrack> {

    override suspend fun map(from: AnimeDetailsResponse): AnimeWithTrack {

        val title = Anime(
            id = 0,
            shikimoriId = from.id,
            name = from.name,
            nameRu = from.nameRu,
            nameEn = from.namesEnglish?.firstOrNull(),
            image = imageMapper.map(from.image),
            url = from.url.appendHostIfNeed(),
            animeType = typeMapper.map(from.type),
            rating = from.score,
            status = titleStatusMapper.map(from.status),
            episodes = from.episodes,
            episodesAired = from.episodesAired,
            dateAired = from.dateAired,
            dateReleased = from.dateReleased,
            nextEpisodeDate = from.nextEpisodeDate,
            ageRating = ageRatingMapper.map(from.ageRating),
            duration = from.duration,
            description = from.description,
            descriptionHtml = from.descriptionHtml,
            franchise = from.franchise,
            favorite = from.favoured,
            topicId = from.topicId,
            genres = from.genres.mapNotNull { genreMapper.map(it) },
        )

        val rate = from.userRate?.let { rateMapper.map(it to TrackTargetType.ANIME) }

        return AnimeWithTrack(
            entity = title,
            track = rate,
            pinned = false
        )
    }
}
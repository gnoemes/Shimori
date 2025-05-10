package com.gnoemes.shimori.data.source.mapper

import com.gnoemes.shimori.base.utils.Mapper
import com.gnoemes.shimori.data.common.AgeRating
import com.gnoemes.shimori.data.common.TitleStatus
import com.gnoemes.shimori.data.titles.anime.Anime
import com.gnoemes.shimori.data.titles.anime.AnimeType
import com.gnoemes.shimori.source.model.SAnime
import me.tatarka.inject.annotations.Inject

@Inject
class SourceAnimeEntityMapper(
    private val imageMapper: SourceImageMapper,
) : Mapper<SAnime, Anime> {

    override fun map(from: SAnime): Anime {
        return Anime(
            id = from.id,
            name = from.name,
            nameRu = from.nameRu,
            nameEn = from.nameEn,
            image = from.image?.let { imageMapper.map(it) },
            url = from.url,
            animeType = AnimeType.find(from.animeType),
            rating = from.rating,
            status = TitleStatus.find(from.status),
            episodes = from.episodes,
            episodesAired = from.episodesAired,
            dateAired = from.dateAired,
            dateReleased = from.dateReleased,
            nextEpisode = from.nextEpisode,
            nextEpisodeDate = from.nextEpisodeDate,
            ageRating = AgeRating.find(from.ageRating),
            duration = from.duration,
            description = from.description,
            descriptionHtml = from.descriptionHtml,
            franchise = from.franchise,
            favorite = from.favorite,
            topicId = from.topicId,
            dubbers = from.fanDubbers,
            subbers = from.fanSubbers
        )
    }
}
package com.gnoemes.shimori.data.source.mapper

import com.gnoemes.shimori.base.utils.Mapper
import com.gnoemes.shimori.base.utils.invoke
import com.gnoemes.shimori.data.common.AgeRating
import com.gnoemes.shimori.data.common.TitleStatus
import com.gnoemes.shimori.data.titles.anime.Anime
import com.gnoemes.shimori.data.titles.anime.AnimeInfo
import com.gnoemes.shimori.data.titles.anime.AnimeScreenshot
import com.gnoemes.shimori.data.titles.anime.AnimeType
import com.gnoemes.shimori.data.titles.anime.AnimeVideo
import com.gnoemes.shimori.data.titles.anime.AnimeVideoType
import com.gnoemes.shimori.source.model.SAnime
import me.tatarka.inject.annotations.Inject

@Inject
class SourceAnimeMapper(
    private val imageMapper: SourceImageMapper,
    private val trackMapper: SourceTrackMapper,
    private val characterMapper: SourceCharacterMapper,
    private val roleMapper: SourceCharacterRoleMapper,
    private val genreMapper: SourceGenreMapper,
) : Mapper<SAnime, AnimeInfo> {

    override fun map(from: SAnime): AnimeInfo {
        val entity = Anime(
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
            topicId = from.topicId
        )

        val videos = from.videos?.map {
            AnimeVideo(
                id = it.id,
                titleId = it.titleId,
                name = it.name,
                url = it.url,
                imageUrl = it.imageUrl,
                type = AnimeVideoType.find(it.type)
            )
        }

        val screenshots = from.screenshots?.map {
            AnimeScreenshot(
                id = it.id,
                titleId = it.titleId,
                image = imageMapper.map(it.image)
            )
        }

        val characters = from.characters?.map { characterMapper(it) }
        val characterRoles = from.charactersRoles?.mapNotNull { roleMapper(it) }

        val genres = from.genres
            ?.map { genreMapper.map(it) }

        return AnimeInfo(
            entity = entity,
            track = from.track?.let { trackMapper.map(it) },
            videos = videos,
            screenshots = screenshots,
            characters = characters,
            charactersRoles = characterRoles,
            genres = genres
        )
    }

}
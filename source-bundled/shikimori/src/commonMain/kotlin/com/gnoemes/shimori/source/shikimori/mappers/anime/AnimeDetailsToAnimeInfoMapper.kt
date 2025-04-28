package com.gnoemes.shimori.source.shikimori.mappers.anime

import com.gnoemes.shimori.base.utils.Mapper
import com.gnoemes.shimori.source.model.SAnime
import com.gnoemes.shimori.source.model.SAnimeScreenshot
import com.gnoemes.shimori.source.model.SAnimeVideo
import com.gnoemes.shimori.source.model.SGenre
import com.gnoemes.shimori.source.model.SImage
import com.gnoemes.shimori.source.shikimori.AnimeDetailsQuery
import com.gnoemes.shimori.source.shikimori.Shikimori.Companion.appendHostIfNeed
import com.gnoemes.shimori.source.shikimori.Shikimori.Companion.removeBbCodes
import com.gnoemes.shimori.source.shikimori.ShikimoriValues
import com.gnoemes.shimori.source.shikimori.mappers.StudioFragmentMapper
import com.gnoemes.shimori.source.shikimori.mappers.toInstant
import com.gnoemes.shimori.source.shikimori.mappers.toLocalDate
import com.gnoemes.shimori.source.shikimori.mappers.toSourceType
import com.gnoemes.shimori.source.shikimori.type.GenreKindEnum
import me.tatarka.inject.annotations.Inject

@Inject
class AnimeDetailsToAnimeInfoMapper(
    private val values: ShikimoriValues,
    private val studioMapper: StudioFragmentMapper,
) : Mapper<AnimeDetailsQuery.Anime, SAnime> {

    override fun map(from: AnimeDetailsQuery.Anime): SAnime {
        val fandubbers = from.fandubbers
        val fansubbers = from.fansubbers

        val videos = from.videos.map {
            SAnimeVideo(
                titleId = from.id.toLong(),
                name = it.name,
                type = it.kind.toSourceType(),
                url = it.url,
                imageUrl = "https:${it.imageUrl}"
            )
        }

        val screenshots = from.screenshots.map {
            SAnimeScreenshot(
                titleId = from.id.toLong(),
                image = SImage(
                    original = it.originalUrl,
                    preview = it.x332Url,
                    x96 = it.x166Url
                )
            )
        }

        val genres = from.genres
            ?.map { it.genre }
            ?.map {
                SGenre(
                    id = it.id.toLong(),
                    name = it.name,
                    nameRu = it.russian,
                    type = when (it.kind) {
                        GenreKindEnum.genre -> 0
                        else -> 1
                    },
                    description = null
                )
            }

        val studio = from.studios.firstOrNull()?.let { studioMapper.map(it.studio) }

        val title = SAnime(
            id = from.id.toLong(),
            name = from.name,
            nameRu = from.russian,
            nameEn = from.english,
            image = from.poster?.posterShort?.toSourceType(),
            animeType = from.kind?.toSourceType(),
            url = from.url.appendHostIfNeed(values),
            rating = from.score,
            status = from.status.toSourceType(),
            episodes = from.episodes,
            episodesAired = from.episodesAired,
            dateAired = from.airedOn?.date?.toLocalDate(),
            dateReleased = from.releasedOn?.date?.toLocalDate(),
            nextEpisode = from.episodesAired + 1,
            nextEpisodeDate = from.nextEpisodeAt?.toInstant(),
            ageRating = from.rating.toSourceType(),
            duration = from.duration,
            description = from.description?.removeBbCodes(),
            descriptionHtml = from.descriptionHtml,
            franchise = from.franchise,
            genres = genres,
            videos = videos,
            screenshots = screenshots,
            fanDubbers = fandubbers,
            fanSubbers = fansubbers,
            characters = null,
            charactersRoles = null,
            studio = studio
        )


        return SAnime(
            entity = title,
            track = null,
        )
    }
}

package com.gnoemes.shimori.sources.shikimori.mappers.anime

import com.gnoemes.shimori.base.utils.Mapper
import com.gnoemes.shimori.data.characters.Character
import com.gnoemes.shimori.data.characters.CharacterRole
import com.gnoemes.shimori.data.common.ShimoriImage
import com.gnoemes.shimori.data.titles.anime.Anime
import com.gnoemes.shimori.data.titles.anime.AnimeInfo
import com.gnoemes.shimori.data.titles.anime.AnimeScreenshot
import com.gnoemes.shimori.data.titles.anime.AnimeVideo
import com.gnoemes.shimori.data.track.TrackTargetType
import com.gnoemes.shimori.sources.shikimori.AnimeDetailsQuery
import com.gnoemes.shimori.sources.shikimori.Shikimori.Companion.appendHostIfNeed
import com.gnoemes.shimori.sources.shikimori.ShikimoriValues
import com.gnoemes.shimori.sources.shikimori.mappers.toInstant
import com.gnoemes.shimori.sources.shikimori.mappers.toLocalDate
import com.gnoemes.shimori.sources.shikimori.mappers.toShimoriImage
import com.gnoemes.shimori.sources.shikimori.mappers.toShimoriType
import me.tatarka.inject.annotations.Inject

@Inject
class AnimeDetailsToAnimeInfoMapper(
    private val values: ShikimoriValues,
) : Mapper<AnimeDetailsQuery.Anime, AnimeInfo> {

    override fun map(from: AnimeDetailsQuery.Anime): AnimeInfo {
        val title = Anime(
            id = from.id.toLong(),
            name = from.name,
            nameRu = from.russian,
            nameEn = from.japanese,
            image = from.poster?.posterShort?.toShimoriImage(),
            type = TrackTargetType.ANIME,
            animeType = from.kind?.toShimoriType(),
            url = from.url.appendHostIfNeed(values),
            rating = from.score,
            status = from.status.toShimoriType(),
            episodes = from.episodes,
            episodesAired = from.episodesAired,
            dateAired = from.airedOn?.date?.toLocalDate(),
            dateReleased = from.releasedOn?.date?.toLocalDate(),
            nextEpisode = from.episodesAired + 1,
            nextEpisodeDate = from.nextEpisodeAt?.toInstant(),
            ageRating = from.rating.toShimoriType(),
            duration = from.duration,
            description = from.description,
            descriptionHtml = from.descriptionHtml,
            franchise = from.franchise,
            genres = from.genres?.map { it.toShimoriType() }
        )

        val fandubbers = from.fandubbers
        val fansubbers = from.fansubbers

        val track = from.userRate?.animeUserRate?.toShimoriType()

        val videos = from.videos.map {
            AnimeVideo(
                titleId = title.id,
                name = it.name,
                type = it.kind.toShimoriType()
            )
        }

        val screenshots = from.screenshots.map {
            AnimeScreenshot(
                titleId = title.id,
                image = ShimoriImage(
                    original = it.originalUrl,
                    preview = it.x332Url,
                    x96 = it.x166Url
                )
            )
        }

        val characters = from.characterRoles
            ?.map { it.character.characterShort }
            ?.map { character ->
                Character(
                    id = character.id.toLong(),
                    name = character.name,
                    nameRu = character.russian,
                    nameEn = character.japanese,
                    image = character.poster?.posterShort?.toShimoriImage(),
                    url = character.url.appendHostIfNeed(values),
                )
            } ?: emptyList()

        val characterRoles = from.characterRoles?.map {
            CharacterRole(
                characterId = it.character.characterShort.id.toLong(),
                targetId = from.id.toLong(),
                targetType = TrackTargetType.ANIME
            )
        } ?: emptyList()


        return AnimeInfo(
            entity = title,
            track = track,
            videos = videos,
            screenshots = screenshots,
            fanDubbers = fandubbers,
            fanSubbers = fansubbers,
            characters = characters,
            charactersRoles = characterRoles
        )
    }
}

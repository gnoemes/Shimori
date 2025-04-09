package com.gnoemes.shimori.source.shikimori.mappers.anime

import com.gnoemes.shimori.base.utils.Mapper
import com.gnoemes.shimori.source.model.SAnime
import com.gnoemes.shimori.source.model.SAnimeScreenshot
import com.gnoemes.shimori.source.model.SAnimeVideo
import com.gnoemes.shimori.source.model.SCharacter
import com.gnoemes.shimori.source.model.SCharacterRole
import com.gnoemes.shimori.source.model.SImage
import com.gnoemes.shimori.source.model.SourceDataType
import com.gnoemes.shimori.source.shikimori.AnimeDetailsQuery
import com.gnoemes.shimori.source.shikimori.Shikimori.Companion.appendHostIfNeed
import com.gnoemes.shimori.source.shikimori.ShikimoriValues
import com.gnoemes.shimori.source.shikimori.mappers.toInstant
import com.gnoemes.shimori.source.shikimori.mappers.toLocalDate
import com.gnoemes.shimori.source.shikimori.mappers.toSourceType
import me.tatarka.inject.annotations.Inject

@Inject
class AnimeDetailsToAnimeInfoMapper(
    private val values: ShikimoriValues,
) : Mapper<AnimeDetailsQuery.Anime, SAnime> {

    override fun map(from: AnimeDetailsQuery.Anime): SAnime {
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
            description = from.description,
            descriptionHtml = from.descriptionHtml,
            franchise = from.franchise,
            genres = from.genres?.map { it.toSourceType() }
        )

        val fandubbers = from.fandubbers
        val fansubbers = from.fansubbers

        val track = from.userRate?.animeUserRate?.toSourceType()

        val videos = from.videos.map {
            SAnimeVideo(
                titleId = title.id,
                name = it.name,
                type = it.kind.toSourceType()
            )
        }

        val screenshots = from.screenshots.map {
            SAnimeScreenshot(
                titleId = title.id,
                image = SImage(
                    original = it.originalUrl,
                    preview = it.x332Url,
                    x96 = it.x166Url
                )
            )
        }

        val characters = from.characterRoles
            ?.map { it.character.characterShort }
            ?.map { character ->
                SCharacter(
                    id = character.id.toLong(),
                    name = character.name,
                    nameRu = character.russian,
                    nameEn = character.name,
                    image = character.poster?.posterShort?.toSourceType(),
                    url = character.url.appendHostIfNeed(values),
                )
            } ?: emptyList()

        val characterRoles = from.characterRoles?.map {
            SCharacterRole(
                characterId = it.character.characterShort.id.toLong(),
                targetId = from.id.toLong(),
                targetType = SourceDataType.Anime
            )
        } ?: emptyList()


        return SAnime(
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

package com.gnoemes.shimori.source.shikimori.mappers.anime

import com.gnoemes.shimori.base.utils.Mapper
import com.gnoemes.shimori.base.utils.invoke
import com.gnoemes.shimori.source.model.SAnime
import com.gnoemes.shimori.source.model.SAnimeScreenshot
import com.gnoemes.shimori.source.model.SAnimeVideo
import com.gnoemes.shimori.source.model.SCharacterRole
import com.gnoemes.shimori.source.model.SGenre
import com.gnoemes.shimori.source.model.SImage
import com.gnoemes.shimori.source.model.SourceDataType
import com.gnoemes.shimori.source.shikimori.AnimeDetailsQuery
import com.gnoemes.shimori.source.shikimori.Shikimori.Companion.appendHostIfNeed
import com.gnoemes.shimori.source.shikimori.ShikimoriValues
import com.gnoemes.shimori.source.shikimori.mappers.character.CharacterShortToSCharacterMapper
import com.gnoemes.shimori.source.shikimori.mappers.toInstant
import com.gnoemes.shimori.source.shikimori.mappers.toLocalDate
import com.gnoemes.shimori.source.shikimori.mappers.toSourceType
import com.gnoemes.shimori.source.shikimori.type.GenreKindEnum
import me.tatarka.inject.annotations.Inject

@Inject
class AnimeDetailsToAnimeInfoMapper(
    private val values: ShikimoriValues,
    private val characterMapper: CharacterShortToSCharacterMapper,
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
                imageUrl = it.imageUrl
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

        val characters = from.characterRoles
            ?.map { it.character.characterShort }
            ?.map { characterMapper(it) }

        val characterRoles = from.characterRoles?.map {
            SCharacterRole(
                characterId = it.character.characterShort.id.toLong(),
                targetId = from.id.toLong(),
                targetType = SourceDataType.Anime,
                role = it.rolesEn.firstOrNull(),
                roleRu = it.rolesRu.firstOrNull()
            )
        }

        val genres = from.genres?.map {
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
            genres = genres,
            videos = videos,
            screenshots = screenshots,
            fanDubbers = fandubbers,
            fanSubbers = fansubbers,
            characters = characters,
            charactersRoles = characterRoles
        )


        return SAnime(
            entity = title,
            track = null,
        )
    }
}

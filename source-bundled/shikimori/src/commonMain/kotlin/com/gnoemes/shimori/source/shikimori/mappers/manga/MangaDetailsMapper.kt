package com.gnoemes.shimori.source.shikimori.mappers.manga

import com.gnoemes.shimori.base.utils.Mapper
import com.gnoemes.shimori.base.utils.invoke
import com.gnoemes.shimori.source.model.SCharacterRole
import com.gnoemes.shimori.source.model.SManga
import com.gnoemes.shimori.source.model.SourceDataType
import com.gnoemes.shimori.source.shikimori.MangaDetailsQuery
import com.gnoemes.shimori.source.shikimori.Shikimori.Companion.appendHostIfNeed
import com.gnoemes.shimori.source.shikimori.ShikimoriValues
import com.gnoemes.shimori.source.shikimori.mappers.character.CharacterShortToSCharacterMapper
import com.gnoemes.shimori.source.shikimori.mappers.toLocalDate
import com.gnoemes.shimori.source.shikimori.mappers.toSourceRanobeType
import com.gnoemes.shimori.source.shikimori.mappers.toSourceType
import me.tatarka.inject.annotations.Inject

@Inject
class MangaDetailsMapper(
    private val values: ShikimoriValues,
    private val characterMapper: CharacterShortToSCharacterMapper,
) : Mapper<MangaDetailsQuery.Manga, SManga> {

    override fun map(from: MangaDetailsQuery.Manga): SManga {

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

        val mangaType = from.kind?.toSourceType()
        val ranobeType = from.kind?.toSourceRanobeType()

        val types =
            if (mangaType != null) SourceDataType.Manga to mangaType
            else SourceDataType.Ranobe to ranobeType

        val title = SManga(
            id = from.id.toLong(),
            name = from.name,
            nameRu = from.russian,
            nameEn = from.english,
            image = from.poster?.posterShort?.toSourceType(),
            type = types.first,
            mangaType = types.second,
            url = from.url.appendHostIfNeed(values),
            rating = from.score,
            status = from.status.toSourceType(),
            chapters = from.chapters,
            volumes = from.volumes,
            dateAired = from.airedOn?.date?.toLocalDate(),
            dateReleased = from.releasedOn?.date?.toLocalDate(),
            ageRating = null,
            characters = characters,
            charactersRoles = characterRoles
        )


        return SManga(
            entity = title,
            track = null,
        )
    }
}
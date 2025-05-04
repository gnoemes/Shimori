package com.gnoemes.shimori.source.shikimori.mappers.anime

import com.gnoemes.shimori.base.utils.Mapper
import com.gnoemes.shimori.base.utils.invoke
import com.gnoemes.shimori.source.model.SAnime
import com.gnoemes.shimori.source.model.SPersonRole
import com.gnoemes.shimori.source.model.SourceDataType
import com.gnoemes.shimori.source.shikimori.AnimePersonsQuery
import com.gnoemes.shimori.source.shikimori.mappers.person.PersonShortToSPersonMapper
import me.tatarka.inject.annotations.Inject

@Inject
class AnimePersonsMapper(
    private val personMapper: PersonShortToSPersonMapper,
) : Mapper<AnimePersonsQuery.Anime, SAnime> {

    override fun map(from: AnimePersonsQuery.Anime): SAnime {
        val persons = from.personRoles
            ?.map { it.person.personShort }
            ?.map { personMapper(it) }

        val personsRoles = from.personRoles?.map {
            SPersonRole(
                personId = it.person.personShort.id.toLong(),
                targetId = from.id.toLong(),
                targetType = SourceDataType.Anime,
                role = it.rolesEn.firstOrNull(),
                roleRu = it.rolesRu.firstOrNull()
            )
        }

        return SAnime(
            id = from.id.toLong(),
            persons = persons,
            personsRoles = personsRoles
        )
    }
}
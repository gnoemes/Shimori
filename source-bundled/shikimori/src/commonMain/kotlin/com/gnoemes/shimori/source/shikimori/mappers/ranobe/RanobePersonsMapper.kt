package com.gnoemes.shimori.source.shikimori.mappers.ranobe

import com.gnoemes.shimori.base.utils.Mapper
import com.gnoemes.shimori.base.utils.invoke
import com.gnoemes.shimori.source.model.SManga
import com.gnoemes.shimori.source.model.SPersonRole
import com.gnoemes.shimori.source.model.SourceDataType
import com.gnoemes.shimori.source.shikimori.MangaPeopleQuery
import com.gnoemes.shimori.source.shikimori.mappers.person.PersonShortToSPersonMapper
import me.tatarka.inject.annotations.Inject

@Inject
class RanobePersonsMapper(
    private val personMapper: PersonShortToSPersonMapper,
) : Mapper<MangaPeopleQuery.Manga, SManga> {

    override fun map(from: MangaPeopleQuery.Manga): SManga {
        val persons = from.personRoles
            ?.map { it.person.personShort }
            ?.map { personMapper(it) }

        val personsRoles = from.personRoles?.map {
            SPersonRole(
                personId = it.person.personShort.id.toLong(),
                targetId = from.id.toLong(),
                targetType = SourceDataType.Ranobe,
                role = it.rolesEn.firstOrNull(),
                roleRu = it.rolesRu.firstOrNull()
            )
        }

        return SManga(
            id = from.id.toLong(),
            type = SourceDataType.Ranobe,
            persons = persons,
            personsRoles = personsRoles
        )
    }
}
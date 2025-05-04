package com.gnoemes.shimori.source.shikimori.mappers.person

import com.gnoemes.shimori.base.utils.Mapper
import com.gnoemes.shimori.source.model.SPerson
import com.gnoemes.shimori.source.shikimori.Shikimori.Companion.appendHostIfNeed
import com.gnoemes.shimori.source.shikimori.ShikimoriValues
import com.gnoemes.shimori.source.shikimori.fragment.PersonShort
import com.gnoemes.shimori.source.shikimori.mappers.toLocalDate
import com.gnoemes.shimori.source.shikimori.mappers.toSourceType
import me.tatarka.inject.annotations.Inject

@Inject
class PersonShortToSPersonMapper(
    private val values: ShikimoriValues
) : Mapper<PersonShort, SPerson> {

    override fun map(from: PersonShort): SPerson {
        return SPerson(
            id = from.id.toLong(),
            name = from.name,
            nameRu = from.russian,
            nameEn = from.name,
            image = from.poster?.posterShort?.toSourceType(),
            url = from.url.appendHostIfNeed(values),
            isMangaka = from.isMangaka,
            isProducer = from.isProducer,
            isSeyu = from.isSeyu,
            birthDate = from.birthOn?.date?.toLocalDate(),
            deceasedDate = from.deceasedOn?.date?.toLocalDate()
        )
    }
}
package com.gnoemes.shimori.data.source.mapper

import com.gnoemes.shimori.base.utils.Mapper
import com.gnoemes.shimori.data.person.Person
import com.gnoemes.shimori.data.person.PersonInfo
import com.gnoemes.shimori.source.model.SPerson
import me.tatarka.inject.annotations.Inject

@Inject
class SourcePersonMapper(
    private val imageMapper: SourceImageMapper,
) : Mapper<SPerson, PersonInfo> {

    override fun map(from: SPerson): PersonInfo {
        val person = Person(
            id = from.id,
            name = from.name,
            nameRu = from.nameRu,
            nameEn = from.nameEn,
            image = from.image?.let { imageMapper.map(it) },
            url = from.url,
            isMangaka = from.isMangaka,
            isProducer = from.isProducer,
            isSeyu = from.isSeyu,
            birthDate = from.birthDate,
            deceasedDate = from.deceasedDate,
        )
        return PersonInfo(
            entity = person,
        )
    }
}
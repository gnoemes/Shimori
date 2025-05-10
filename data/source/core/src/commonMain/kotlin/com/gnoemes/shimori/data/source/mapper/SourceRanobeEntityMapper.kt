package com.gnoemes.shimori.data.source.mapper

import com.gnoemes.shimori.base.utils.Mapper
import com.gnoemes.shimori.data.common.AgeRating
import com.gnoemes.shimori.data.common.TitleStatus
import com.gnoemes.shimori.data.titles.ranobe.Ranobe
import com.gnoemes.shimori.data.titles.ranobe.RanobeType
import com.gnoemes.shimori.source.model.SManga
import me.tatarka.inject.annotations.Inject

@Inject
class SourceRanobeEntityMapper(
    private val imageMapper: SourceImageMapper,
) : Mapper<SManga, Ranobe> {

    override fun map(from: SManga): Ranobe {
       return Ranobe(
            id = from.id,
            name = from.name,
            nameRu = from.nameRu,
            nameEn = from.nameEn,
            image = from.image?.let { imageMapper.map(it) },
            url = from.url,
            ranobeType = RanobeType.find(from.mangaType),
            rating = from.rating,
            status = TitleStatus.find(from.status),
            volumes = from.volumes,
            chapters = from.chapters,
            dateAired = from.dateAired,
            dateReleased = from.dateReleased,
            ageRating = AgeRating.find(from.ageRating),
            description = from.description,
            descriptionHtml = from.descriptionHtml,
            franchise = from.franchise,
            favorite = from.favorite,
            topicId = from.topicId,
        )
    }
}
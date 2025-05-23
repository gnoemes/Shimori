package com.gnoemes.shimori.data.source.mapper

import com.gnoemes.shimori.base.utils.Mapper
import com.gnoemes.shimori.data.common.AgeRating
import com.gnoemes.shimori.data.common.TitleStatus
import com.gnoemes.shimori.data.titles.manga.Manga
import com.gnoemes.shimori.data.titles.manga.MangaType
import com.gnoemes.shimori.source.model.SManga
import me.tatarka.inject.annotations.Inject

@Inject
class SourceMangaEntityMapper(
    private val imageMapper: SourceImageMapper,
) : Mapper<SManga, Manga> {

    override fun map(from: SManga): Manga {
        return Manga(
            id = from.id,
            name = from.name,
            nameRu = from.nameRu,
            nameEn = from.nameEn,
            image = from.image?.let { imageMapper.map(it) },
            url = from.url,
            mangaType = MangaType.find(from.mangaType),
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
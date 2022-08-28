package com.gnoemes.shikimori.mappers.ranobe

import com.gnoemes.shikimori.entities.manga.MangaDetailsResponse
import com.gnoemes.shikimori.mappers.AgeRatingMapper
import com.gnoemes.shikimori.mappers.GenreMapper
import com.gnoemes.shikimori.mappers.ImageResponseMapper
import com.gnoemes.shikimori.mappers.TitleStatusMapper
import com.gnoemes.shikimori.mappers.rate.RateResponseToRateMapper
import com.gnoemes.shimori.data.core.entities.rate.RateTargetType
import com.gnoemes.shimori.data.core.entities.titles.ranobe.Ranobe
import com.gnoemes.shimori.data.core.entities.titles.ranobe.RanobeWithRate
import com.gnoemes.shimori.data.core.mappers.Mapper

internal class RanobeDetailsMapper(
    private val imageMapper: ImageResponseMapper,
    private val typeMapper: RanobeTypeMapper,
    private val titleStatusMapper: TitleStatusMapper,
    private val rateMapper: RateResponseToRateMapper,
    private val ageRatingMapper: AgeRatingMapper,
    private val genreMapper: GenreMapper
) : Mapper<MangaDetailsResponse, RanobeWithRate> {

    override suspend fun map(from: MangaDetailsResponse): RanobeWithRate {

        val title = Ranobe(
            id = 0,
            shikimoriId = from.id,
            name = from.name,
            nameRu = from.nameRu,
            nameEn = from.namesEnglish?.firstOrNull(),
            image = imageMapper.map(from.image),
            url = from.url,
            ranobeType = typeMapper.map(from.type),
            rating = from.score,
            status = titleStatusMapper.map(from.status),
            chapters = from.chapters,
            volumes = from.volumes,
            dateAired = from.dateAired,
            dateReleased = from.dateReleased,
            ageRating = ageRatingMapper.map(from.ageRating),
            description = from.description,
            descriptionHtml = from.descriptionHtml,
            franchise = from.franchise,
            favorite = from.favoured,
            topicId = from.topicId,
            genres = from.genres.mapNotNull { genreMapper.map(it) },
        )

        val rate = from.userRate?.let { rateMapper.map(it to RateTargetType.RANOBE) }

        return RanobeWithRate(
            entity = title,
            rate = rate,
            pinned = false
        )
    }
}
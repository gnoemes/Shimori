package com.gnoemes.shikimori.mappers.ranobe

import com.gnoemes.shikimori.appendHostIfNeed
import com.gnoemes.shikimori.entities.manga.MangaDetailsResponse
import com.gnoemes.shikimori.mappers.AgeRatingMapper
import com.gnoemes.shikimori.mappers.GenreMapper
import com.gnoemes.shikimori.mappers.ImageResponseMapper
import com.gnoemes.shikimori.mappers.TitleStatusMapper
import com.gnoemes.shimori.data.core.entities.titles.ranobe.Ranobe
import com.gnoemes.shimori.data.core.entities.titles.ranobe.RanobeWithTrack
import com.gnoemes.shimori.data.core.mappers.Mapper

internal class RanobeDetailsMapper(
    private val imageMapper: ImageResponseMapper,
    private val typeMapper: RanobeTypeMapper,
    private val titleStatusMapper: TitleStatusMapper,
    private val ageRatingMapper: AgeRatingMapper,
    private val genreMapper: GenreMapper
) : Mapper<MangaDetailsResponse, RanobeWithTrack> {

    override suspend fun map(from: MangaDetailsResponse): RanobeWithTrack {

        val title = Ranobe(
            id = from.id,
            name = from.name,
            nameRu = from.nameRu,
            nameEn = from.namesEnglish?.firstOrNull(),
            image = imageMapper.map(from.image),
            url = from.url.appendHostIfNeed(),
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

        return RanobeWithTrack(
            entity = title,
            track = null,
            pinned = false
        )
    }
}
package com.gnoemes.shikimori.mappers.manga

import com.gnoemes.shikimori.entities.manga.MangaResponse
import com.gnoemes.shikimori.mappers.ImageResponseMapper
import com.gnoemes.shimori.data_base.mappers.Mapper
import com.gnoemes.shimori.model.ShimoriConstants
import com.gnoemes.shimori.model.manga.Manga
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class MangaResponseMapper @Inject constructor(
    private val imageMapper: ImageResponseMapper,
    private val typeMapper: MangaTypeMapper
) : Mapper<MangaResponse, Manga> {

    override suspend fun map(from: MangaResponse) = Manga(
            shikimoriId = from.id,
            name = from.name.trim(),
            nameRu = from.nameRu?.trim()?.ifEmpty { null },
            image = imageMapper.map(from.image),
            url = from.url.appendHostIfNeed(),
            _type = typeMapper.map(from.type)?.type,
            score = from.score,
            status = from.status,
            volumes = from.volumes,
            chapters = from.chapters,
            dateAired = from.dateAired,
            dateReleased = from.dateReleased
    )

    private fun String.appendHostIfNeed(host: String = ShimoriConstants.ShikimoriBaseUrl): String {
        return if (this.contains("http")) this else host + this
    }
}
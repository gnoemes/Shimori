package com.gnoemes.shikimori.mappers.manga

import com.gnoemes.shikimori.appendHostIfNeed
import com.gnoemes.shikimori.entities.manga.MangaResponse
import com.gnoemes.shikimori.mappers.ImageResponseMapper
import com.gnoemes.shikimori.mappers.TitleStatusMapper
import com.gnoemes.shimori.data.base.entities.titles.manga.Manga
import com.gnoemes.shimori.data.base.mappers.Mapper

internal class MangaResponseMapper constructor(
    private val imageMapper: ImageResponseMapper,
    private val typeMapper: MangaTypeMapper,
    private val titleStatusMapper: TitleStatusMapper,
) : Mapper<MangaResponse, Manga> {

    override suspend fun map(from: MangaResponse) = Manga(
        shikimoriId = from.id,
        name = from.name.trim(),
        nameRu = from.nameRu?.trim()?.ifEmpty { null },
        image = imageMapper.map(from.image),
        url = from.url.appendHostIfNeed(),
        mangaType = typeMapper.map(from.type),
        rating = from.score,
        status = titleStatusMapper.map(from.status),
        volumes = from.volumes,
        chapters = from.chapters,
        dateAired = from.dateAired,
        dateReleased = from.dateReleased
    )


}
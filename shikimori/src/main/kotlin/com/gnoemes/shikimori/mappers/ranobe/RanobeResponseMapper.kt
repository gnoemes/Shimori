package com.gnoemes.shikimori.mappers.ranobe

import com.gnoemes.shikimori.appendHostIfNeed
import com.gnoemes.shikimori.entities.manga.MangaResponse
import com.gnoemes.shikimori.mappers.ImageResponseMapper
import com.gnoemes.shikimori.mappers.TitleStatusMapper
import com.gnoemes.shimori.data.base.entities.titles.ranobe.Ranobe
import com.gnoemes.shimori.data.base.mappers.Mapper

internal class RanobeResponseMapper constructor(
    private val imageMapper: ImageResponseMapper,
    private val typeMapper: RanobeTypeMapper,
    private val titleStatusMapper: TitleStatusMapper
) : Mapper<MangaResponse, Ranobe> {

    override suspend fun map(from: MangaResponse) = Ranobe(
        shikimoriId = from.id,
        name = from.name.trim(),
        nameRu = from.nameRu?.trim()?.ifEmpty { null },
        image = imageMapper.map(from.image),
        url = from.url.appendHostIfNeed(),
        ranobe_type = typeMapper.map(from.type)?.type,
        rating = from.score,
        status = titleStatusMapper.map(from.status),
        volumes = from.volumes,
        chapters = from.chapters,
        dateAired = from.dateAired,
        dateReleased = from.dateReleased
    )
}
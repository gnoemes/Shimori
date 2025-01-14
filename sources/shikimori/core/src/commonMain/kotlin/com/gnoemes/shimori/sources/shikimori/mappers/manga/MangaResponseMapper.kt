package com.gnoemes.shimori.sources.shikimori.mappers.manga

import com.gnoemes.shimori.base.utils.Mapper
import com.gnoemes.shimori.data.titles.manga.Manga
import com.gnoemes.shimori.sources.shikimori.Shikimori.Companion.appendHostIfNeed
import com.gnoemes.shimori.sources.shikimori.ShikimoriValues
import com.gnoemes.shimori.sources.shikimori.mappers.ImageResponseMapper
import com.gnoemes.shimori.sources.shikimori.mappers.TitleStatusMapper
import com.gnoemes.shimori.sources.shikimori.models.manga.MangaResponse
import me.tatarka.inject.annotations.Inject

@Inject
class MangaResponseMapper constructor(
    private val imageMapper: ImageResponseMapper,
    private val typeMapper: MangaTypeMapper,
    private val titleStatusMapper: TitleStatusMapper,
    private val values: ShikimoriValues,
) : Mapper<MangaResponse, Manga> {

    override fun map(from: MangaResponse) = Manga(
        id = from.id,
        name = from.name.trim(),
        nameRu = from.nameRu?.trim()?.ifEmpty { null },
        image = imageMapper.map(from.image),
        url = from.url.appendHostIfNeed(values),
        mangaType = typeMapper.map(from.type),
        rating = from.score,
        status = titleStatusMapper.map(from.status),
        volumes = from.volumes,
        chapters = from.chapters,
        dateAired = from.dateAired,
        dateReleased = from.dateReleased
    )


}
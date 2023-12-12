package com.gnoemes.shimori.data.shikimori.mappers.manga

import com.gnoemes.shimori.base.utils.Mapper
import com.gnoemes.shimori.data.shikimori.Shikimori.Companion.appendHostIfNeed
import com.gnoemes.shimori.data.shikimori.ShikimoriValues
import com.gnoemes.shimori.data.shikimori.mappers.ImageResponseMapper
import com.gnoemes.shimori.data.shikimori.mappers.TitleStatusMapper
import com.gnoemes.shimori.data.shikimori.models.manga.MangaResponse
import com.gnoemes.shimori.data.titles.manga.Manga
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
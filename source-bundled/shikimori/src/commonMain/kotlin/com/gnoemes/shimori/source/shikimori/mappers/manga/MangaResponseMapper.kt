package com.gnoemes.shimori.source.shikimori.mappers.manga

import com.gnoemes.shimori.base.utils.Mapper
import com.gnoemes.shimori.source.model.SManga
import com.gnoemes.shimori.source.model.SourceDataType
import com.gnoemes.shimori.source.shikimori.Shikimori.Companion.appendHostIfNeed
import com.gnoemes.shimori.source.shikimori.ShikimoriValues
import com.gnoemes.shimori.source.shikimori.mappers.ImageResponseMapper
import com.gnoemes.shimori.source.shikimori.mappers.TitleStatusMapper
import com.gnoemes.shimori.source.shikimori.models.manga.MangaResponse
import me.tatarka.inject.annotations.Inject

@Inject
class MangaResponseMapper(
    private val imageMapper: ImageResponseMapper,
    private val typeMapper: MangaTypeMapper,
    private val titleStatusMapper: TitleStatusMapper,
    private val values: ShikimoriValues,
) : Mapper<MangaResponse, SManga> {

    override fun map(from: MangaResponse) = SManga(
        type = SourceDataType.Manga,
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
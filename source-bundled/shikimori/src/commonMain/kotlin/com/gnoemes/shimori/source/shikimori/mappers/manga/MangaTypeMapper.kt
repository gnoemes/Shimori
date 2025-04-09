package com.gnoemes.shimori.source.shikimori.mappers.manga

import com.gnoemes.shimori.base.utils.Mapper
import com.gnoemes.shimori.source.shikimori.models.manga.ShikimoriMangaType
import me.tatarka.inject.annotations.Inject


@Inject
class MangaTypeMapper : Mapper<ShikimoriMangaType?, String?> {
    override fun map(from: ShikimoriMangaType?) = from?.type
}
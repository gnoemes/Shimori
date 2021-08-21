package com.gnoemes.shikimori.mappers.ranobe

import com.gnoemes.shikimori.entities.manga.ShikimoriMangaType
import com.gnoemes.shimori.data_base.mappers.Mapper
import com.gnoemes.shimori.model.ranobe.RanobeType
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
internal class RanobeTypeMapper @Inject constructor() : Mapper<ShikimoriMangaType?, RanobeType?> {
    override suspend fun map(from: ShikimoriMangaType?) = when (from) {
        ShikimoriMangaType.NOVEL -> RanobeType.Novel
        ShikimoriMangaType.LIGHT_NOVEL -> RanobeType.LightNovel
        else -> null
    }
}
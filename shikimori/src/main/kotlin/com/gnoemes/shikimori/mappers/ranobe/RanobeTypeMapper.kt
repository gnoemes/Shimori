package com.gnoemes.shikimori.mappers.ranobe

import com.gnoemes.shikimori.entities.manga.ShikimoriMangaType
import com.gnoemes.shimori.data.base.entities.titles.ranobe.RanobeType
import com.gnoemes.shimori.data.base.mappers.Mapper


internal class RanobeTypeMapper : Mapper<ShikimoriMangaType?, RanobeType?> {
    override suspend fun map(from: ShikimoriMangaType?) = when (from) {
        ShikimoriMangaType.NOVEL -> RanobeType.Novel
        ShikimoriMangaType.LIGHT_NOVEL -> RanobeType.LightNovel
        else -> null
    }
}
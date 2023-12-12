package com.gnoemes.shimori.data.shikimori.mappers.manga

import com.gnoemes.shimori.base.utils.Mapper
import com.gnoemes.shimori.data.shikimori.models.manga.ShikimoriMangaType
import com.gnoemes.shimori.data.titles.manga.MangaType
import me.tatarka.inject.annotations.Inject


@Inject
class MangaTypeMapper : Mapper<ShikimoriMangaType?, MangaType?> {

    override fun map(from: ShikimoriMangaType?) = when (from) {
        ShikimoriMangaType.MANGA -> MangaType.Manga
        ShikimoriMangaType.MANHUA -> MangaType.Manhua
        ShikimoriMangaType.MANHWA -> MangaType.Manhwa
        ShikimoriMangaType.DOUJIN -> MangaType.Doujin
        ShikimoriMangaType.ONE_SHOT -> MangaType.OneShot
        else -> null
    }
}
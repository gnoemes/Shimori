package com.gnoemes.shikimori.mappers.manga

import com.gnoemes.shikimori.entities.manga.ShikimoriMangaType
import com.gnoemes.shimori.data.core.entities.titles.manga.MangaType
import com.gnoemes.shimori.data.core.mappers.Mapper


internal class MangaTypeMapper : Mapper<ShikimoriMangaType?, MangaType?> {

    override suspend fun map(from: ShikimoriMangaType?) = when (from) {
        ShikimoriMangaType.MANGA -> MangaType.Manga
        ShikimoriMangaType.MANHUA -> MangaType.Manhua
        ShikimoriMangaType.MANHWA -> MangaType.Manhwa
        ShikimoriMangaType.DOUJIN -> MangaType.Doujin
        ShikimoriMangaType.ONE_SHOT -> MangaType.OneShot
        else -> null
    }
}
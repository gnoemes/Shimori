package com.gnoemes.shikimori.mappers.manga

import com.gnoemes.shikimori.entities.manga.ShikimoriMangaType
import com.gnoemes.shimori.data_base.mappers.Mapper
import com.gnoemes.shimori.model.manga.MangaType
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
internal class MangaTypeMapper @Inject constructor() : Mapper<ShikimoriMangaType?, MangaType?> {

    override suspend fun map(from: ShikimoriMangaType?) = when (from) {
        ShikimoriMangaType.MANGA -> MangaType.Manga
        ShikimoriMangaType.MANHUA -> MangaType.Manhua
        ShikimoriMangaType.MANHWA -> MangaType.Manhwa
        ShikimoriMangaType.DOUJIN -> MangaType.Doujin
        ShikimoriMangaType.ONE_SHOT -> MangaType.OneShot
        else -> null
    }
}
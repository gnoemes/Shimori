package com.gnoemes.shikimori.mappers.manga

import com.gnoemes.shikimori.entities.rates.RateResponse
import com.gnoemes.shimori.data.core.entities.titles.manga.Manga
import com.gnoemes.shimori.data.core.mappers.Mapper

internal class RateResponseToMangaMapper constructor(
    private val mangaResponseMapper: MangaResponseMapper
) : Mapper<RateResponse, Manga> {

    override suspend fun map(from: RateResponse): Manga {
        return mangaResponseMapper.map(from.manga!!)
    }
}
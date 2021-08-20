package com.gnoemes.shikimori.mappers.manga

import com.gnoemes.shikimori.entities.rates.RateResponse
import com.gnoemes.shimori.data_base.mappers.Mapper
import com.gnoemes.shimori.model.manga.Manga
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class RateResponseToMangaMapper @Inject constructor(
    private val mangaResponseMapper: MangaResponseMapper
) : Mapper<RateResponse, Manga> {

    override suspend fun map(from: RateResponse): Manga {
        return mangaResponseMapper.map(from.manga!!)
    }
}
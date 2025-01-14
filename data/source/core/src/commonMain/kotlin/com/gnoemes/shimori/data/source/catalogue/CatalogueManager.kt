package com.gnoemes.shimori.data.source.catalogue

import com.gnoemes.shimori.data.app.SourceDataType
import com.gnoemes.shimori.data.app.SourceResponse
import com.gnoemes.shimori.data.source.SourceManager
import com.gnoemes.shimori.data.source.mapper.SourceRequestMapper
import com.gnoemes.shimori.preferences.ShimoriPreferences
import com.gnoemes.shimori.source.CatalogueSource
import com.gnoemes.shimori.source.data.AnimeDataSource
import com.gnoemes.shimori.source.data.CharacterDataSource
import com.gnoemes.shimori.source.data.MangaDataSource
import com.gnoemes.shimori.source.data.RanobeDataSource
import me.tatarka.inject.annotations.Inject

@Inject
class CatalogueManager(
    private val catalogs: Set<CatalogueSource>,
    private val prefs: ShimoriPreferences,
    mapper: SourceRequestMapper,
) : SourceManager<CatalogueSource>(mapper) {
    private val currentCatalog: CatalogueSource
        get() {
            val active = prefs.currentCatalogueSource
            return catalogs
                .find { it.name == active }
                ?: catalogs.first().also { prefs.currentCatalogueSource = it.name }
        }

    suspend fun <ResponseType> anime(
        action: suspend AnimeDataSource.() -> ResponseType
    ): SourceResponse<ResponseType> = with(currentCatalog) {
        return@with request(this, animeDataSource, action)
    }

    suspend fun <RequestType, ResponseType> anime(
        data: RequestType,
        action: suspend AnimeDataSource.(RequestType) -> ResponseType
    ): SourceResponse<ResponseType> = with(currentCatalog) {
        return@with request(this, animeDataSource, mapper, SourceDataType.Anime, data, action)
    }

    suspend fun <ResponseType> manga(
        action: suspend MangaDataSource.() -> ResponseType
    ): SourceResponse<ResponseType> = with(currentCatalog) {
        return@with request(this, mangaDataSource, action)
    }

    suspend fun <RequestType, ResponseType> manga(
        data: RequestType,
        action: suspend MangaDataSource.(RequestType) -> ResponseType
    ): SourceResponse<ResponseType> = with(currentCatalog) {
        return@with request(this, mangaDataSource, mapper, SourceDataType.Manga, data, action)
    }

    suspend fun <ResponseType> ranobe(
        action: suspend RanobeDataSource.() -> ResponseType
    ): SourceResponse<ResponseType> = with(currentCatalog) {
        return@with request(this, ranobeDataSource, action)
    }

    suspend fun <RequestType, ResponseType> ranobe(
        data: RequestType,
        action: suspend RanobeDataSource.(RequestType) -> ResponseType
    ): SourceResponse<ResponseType> = with(currentCatalog) {
        return@with request(this, ranobeDataSource, mapper, SourceDataType.Ranobe, data, action)
    }

    suspend fun <ResponseType> character(
        action: suspend CharacterDataSource.() -> ResponseType
    ): SourceResponse<ResponseType> = with(currentCatalog) {
        return@with request(this, characterDataSource, action)
    }

    suspend fun <RequestType, ResponseType> character(
        data: RequestType,
        action: suspend CharacterDataSource.(RequestType) -> ResponseType
    ): SourceResponse<ResponseType> = with(currentCatalog) {
        return@with request(
            this,
            characterDataSource,
            mapper,
            SourceDataType.Character,
            data,
            action
        )
    }




}
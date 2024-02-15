package com.gnoemes.shimori.data.source.catalogue

import com.gnoemes.shimori.data.app.SourceDataType
import com.gnoemes.shimori.data.app.SourceResponse
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
    private val mapper: SourceRequestMapper,
) {
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

    private suspend fun <DataSource, ResponseType> request(
        catalogue: CatalogueSource,
        dataSource: DataSource,
        action: suspend DataSource.() -> ResponseType
    ) = wrapResponse(catalogue) { action(dataSource) }

    private suspend fun <DataSource, RequestType, ResponseType> request(
        catalogue: CatalogueSource,
        dataSource: DataSource,
        mapper: SourceRequestMapper,
        type: SourceDataType,
        data: RequestType,
        action: suspend DataSource.(RequestType) -> ResponseType
    ) = wrapResponse(catalogue) {
        val preparedData = mapper(id, type, data)
        action(dataSource, preparedData)
    }

    private suspend fun <T> wrapResponse(
        catalogue: CatalogueSource,
        block: suspend CatalogueSource.() -> T
    ) =
        SourceResponse(
            sourceId = catalogue.id,
            data = block(catalogue)
        )

}
package com.gnoemes.shimori.data.source.catalogue

import com.gnoemes.shimori.data.app.SourceResponse
import com.gnoemes.shimori.data.db.api.daos.SourceIdsSyncDao
import com.gnoemes.shimori.data.source.SourceManager
import com.gnoemes.shimori.preferences.ShimoriPreferences
import com.gnoemes.shimori.source.Source
import com.gnoemes.shimori.source.catalogue.AnimeDataSource
import com.gnoemes.shimori.source.catalogue.CatalogueSource
import com.gnoemes.shimori.source.catalogue.CharacterDataSource
import com.gnoemes.shimori.source.catalogue.MangaDataSource
import com.gnoemes.shimori.source.catalogue.RanobeDataSource
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppScope::class)
class CatalogueManager(
    val catalogs: Set<CatalogueSource>,
    private val prefs: ShimoriPreferences,
    private val animeSourceAdapter: AnimeDataSourceAdapter,
    private val mangaSourceAdapter: MangaDataSourceAdapter,
    private val ranobeSourceAdapter: RanobeDataSourceAdapter,
    private val characterSourceAdapter: CharacterDataSourceAdapter,
    dao: SourceIdsSyncDao,
) : SourceManager<CatalogueSource>(dao) {
    private val currentCatalog: CatalogueSource
        get() {
            val active = prefs.currentCatalogueSource
            return catalogs
                .find { it.name == active }
                ?: catalogs.first().also { prefs.currentCatalogueSource = it.name }
        }

    suspend fun <ResponseType> anime(
        action: suspend AnimeDataSourceAdapter.() -> suspend AnimeDataSource.(Source) -> ResponseType
    ): SourceResponse<ResponseType> = with(currentCatalog) {
        val sourceAction = animeSourceAdapter(action)
        return@with request(this, animeDataSource, sourceAction)
    }

    suspend fun <ResponseType> manga(
        action: suspend MangaDataSourceAdapter.() -> suspend MangaDataSource.(Source) -> ResponseType
    ): SourceResponse<ResponseType> = with(currentCatalog) {
        val sourceAction = mangaSourceAdapter(action)
        return@with request(this, mangaDataSource, sourceAction)
    }

    suspend fun <ResponseType> ranobe(
        action: suspend RanobeDataSourceAdapter.() -> suspend RanobeDataSource.(Source) -> ResponseType
    ): SourceResponse<ResponseType> = with(currentCatalog) {
        val sourceAction = ranobeSourceAdapter(action)
        return@with request(this, ranobeDataSource, sourceAction)
    }

    suspend fun <ResponseType> character(
        action: suspend CharacterDataSourceAdapter.() -> suspend CharacterDataSource.(Source) -> ResponseType
    ): SourceResponse<ResponseType> = with(currentCatalog) {
        val sourceAction = characterSourceAdapter(action)
        return@with request(this, characterDataSource, sourceAction)
    }

}
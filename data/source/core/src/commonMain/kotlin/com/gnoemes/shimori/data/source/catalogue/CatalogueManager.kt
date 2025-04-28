package com.gnoemes.shimori.data.source.catalogue

import com.gnoemes.shimori.data.app.SourceResponse
import com.gnoemes.shimori.data.db.api.daos.SourceIdsSyncDao
import com.gnoemes.shimori.data.source.SourceManager
import com.gnoemes.shimori.preferences.ShimoriPreferences
import com.gnoemes.shimori.source.Source
import com.gnoemes.shimori.source.catalogue.AnimeDataSource
import com.gnoemes.shimori.source.catalogue.CatalogueSource
import com.gnoemes.shimori.source.catalogue.CharacterDataSource
import com.gnoemes.shimori.source.catalogue.GenreDataSource
import com.gnoemes.shimori.source.catalogue.MangaDataSource
import com.gnoemes.shimori.source.catalogue.RanobeDataSource
import com.gnoemes.shimori.source.catalogue.StudioDataSource
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
    private val genreSourceAdapter: GenreDataSourceAdapter,
    private val studioSourceAdapter: StudioDataSourceAdapter,
    dao: SourceIdsSyncDao,
) : SourceManager<CatalogueSource>(dao) {
    val current: CatalogueSource
        get() {
            val active = prefs.currentCatalogueSource
            return catalogs
                .find { it.name == active }
                ?: catalogs.first().also(::setActiveCatalogue)
        }

    fun setActiveCatalogue(new: CatalogueSource) {
        val active = prefs.currentCatalogueSource
        if (new.name == active) return

        prefs.currentCatalogueSource = new.name
    }

    fun setActiveCatalogue(sourceId: Long) {
        val new = catalogs.find { it.id == sourceId }
            ?: throw IllegalArgumentException("Catalogue with id $sourceId not found")
        setActiveCatalogue(new)
    }

    suspend fun <ResponseType> anime(
        action: suspend AnimeDataSourceAdapter.() -> suspend AnimeDataSource.(Source) -> ResponseType
    ): SourceResponse<ResponseType> = with(current) {
        val sourceAction = animeSourceAdapter(action)
        return@with request(this, animeDataSource, sourceAction)
    }

    suspend fun <ResponseType> manga(
        action: suspend MangaDataSourceAdapter.() -> suspend MangaDataSource.(Source) -> ResponseType
    ): SourceResponse<ResponseType> = with(current) {
        val sourceAction = mangaSourceAdapter(action)
        return@with request(this, mangaDataSource, sourceAction)
    }

    suspend fun <ResponseType> ranobe(
        action: suspend RanobeDataSourceAdapter.() -> suspend RanobeDataSource.(Source) -> ResponseType
    ): SourceResponse<ResponseType> = with(current) {
        val sourceAction = ranobeSourceAdapter(action)
        return@with request(this, ranobeDataSource, sourceAction)
    }

    suspend fun <ResponseType> character(
        action: suspend CharacterDataSourceAdapter.() -> suspend CharacterDataSource.(Source) -> ResponseType
    ): SourceResponse<ResponseType> = with(current) {
        val sourceAction = characterSourceAdapter(action)
        return@with request(this, characterDataSource, sourceAction)
    }

    suspend fun <ResponseType> genre(
        action: suspend GenreDataSourceAdapter.() -> suspend GenreDataSource.(Source) -> ResponseType
    ): SourceResponse<ResponseType> = with(current) {
        val sourceAction = genreSourceAdapter(action)
        return@with request(this, genreDateSource, sourceAction)
    }

    suspend  fun <ResponseType> studio(
        action: suspend StudioDataSourceAdapter.() -> suspend StudioDataSource.(Source) -> ResponseType
    ): SourceResponse<ResponseType> = with(current) {
        val sourceAction = studioSourceAdapter(action)
        return@with request(this, studioDataSource, sourceAction)
    }

}
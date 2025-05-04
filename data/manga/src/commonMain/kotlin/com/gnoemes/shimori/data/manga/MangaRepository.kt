package com.gnoemes.shimori.data.manga

import com.gnoemes.shimori.data.app.Request
import com.gnoemes.shimori.data.app.SourceResponse
import com.gnoemes.shimori.data.core.BaseCatalogueRepository
import com.gnoemes.shimori.data.db.api.db.DatabaseTransactionRunner
import com.gnoemes.shimori.data.lastrequest.EntityLastRequestStore
import com.gnoemes.shimori.data.source.catalogue.CatalogueManager
import com.gnoemes.shimori.data.titles.manga.Manga
import com.gnoemes.shimori.data.track.ListSort
import com.gnoemes.shimori.data.track.TrackStatus
import com.gnoemes.shimori.data.user.UserShort
import com.gnoemes.shimori.logging.api.Logger
import com.gnoemes.shimori.source.model.SourceDataType
import me.tatarka.inject.annotations.Inject

@Inject
class MangaRepository(
    logger: Logger,
    catalogue: CatalogueManager,
    entityLastRequest: EntityLastRequestStore,
    private val store: SyncedMangaStore,
    private val transactionRunner: DatabaseTransactionRunner
) : BaseCatalogueRepository<Manga>(
    SourceDataType.Manga,
    logger,
    catalogue,
    entityLastRequest,
    transactionRunner
) {
    override fun queryById(id: Long) = store.dao.queryById(id)
    fun observeById(id: Long) = store.dao.observeById(id)
    fun paging(status: TrackStatus, sort: ListSort) = store.dao.paging(status, sort)

    suspend fun syncTracked(
        user: UserShort,
        status: TrackStatus?
    ) = request {
        manga { getWithStatus(user, status) }
    }.also {
        transactionRunner {
            store.trySync(it)
            statusUpdated(status)
        }
    }

    suspend fun sync(id: Long) =
        request(id) {
            manga { get(it) }
        }.also {
            transactionRunner {
                store.trySync(it)
                titleUpdated(id)
            }
        }

    suspend fun syncTitleCharacters(
        id: Long
    ) = request(id) {
        manga { getCharacters(it) }
    }

    suspend fun syncTitlePersons(
        id: Long
    ) = request(id) {
        manga { getPersons(it) }
    }

    override fun <T> trySyncTransaction(data: SourceResponse<T>) {
        store.trySync(data)
    }

    fun shouldUpdateTitlesWithStatus(
        status: TrackStatus?,
    ) = shouldUpdate(
        Request.MANGAS_WITH_STATUS,
        status?.priority?.toLong() ?: TrackStatus.entries.size.toLong(),
    )

    fun shouldUpdateTitle(
        id: Long,
    ) = shouldUpdate(
        Request.MANGA_DETAILS,
        id,
    )

    private fun titleUpdated(id: Long) = updated(Request.MANGA_DETAILS, id)
    fun statusUpdated(status: TrackStatus?) = updated(
        Request.MANGAS_WITH_STATUS,
        status?.priority?.toLong() ?: TrackStatus.entries.size.toLong()
    )
}
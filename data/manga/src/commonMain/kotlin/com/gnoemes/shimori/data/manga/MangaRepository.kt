package com.gnoemes.shimori.data.manga

import com.gnoemes.shimori.base.extensions.inPast
import com.gnoemes.shimori.data.app.ExpiryConstants
import com.gnoemes.shimori.data.app.Request
import com.gnoemes.shimori.data.app.SourceResponse
import com.gnoemes.shimori.data.db.api.db.DatabaseTransactionRunner
import com.gnoemes.shimori.data.lastrequest.EntityLastRequestStore
import com.gnoemes.shimori.data.source.catalogue.CatalogueManager
import com.gnoemes.shimori.data.titles.manga.MangaWithTrack
import com.gnoemes.shimori.data.track.ListSort
import com.gnoemes.shimori.data.track.TrackStatus
import com.gnoemes.shimori.data.user.UserShort
import com.gnoemes.shimori.logging.api.Logger
import kotlinx.datetime.Instant
import me.tatarka.inject.annotations.Inject
import kotlin.time.Duration.Companion.minutes

@Inject
class MangaRepository(
    private val catalogue: CatalogueManager,
    private val store: SyncedMangaStore,
    private val entityLastRequest: EntityLastRequestStore,
    private val logger: Logger,
    private val transactionRunner: DatabaseTransactionRunner
) {

    fun observeById(id: Long) = store.dao.observeById(id)
    fun paging(status: TrackStatus, sort: ListSort) = store.dao.paging(status, sort)

    suspend fun syncTracked(
        user: UserShort,
        status: TrackStatus?
    ): SourceResponse<List<MangaWithTrack>> {
        return catalogue.manga { getWithStatus(user, status) }
            .also {
                transactionRunner {
                    store.trySync(it)
                    statusUpdated(status)
                }
            }
    }

    suspend fun sync(id: Long): SourceResponse<MangaWithTrack> {
        val local = store.dao.queryById(id)
            ?: throw IllegalStateException("Anime with id: $id not found")

        return catalogue.manga { get(local) }
            .also {
                transactionRunner {
                    store.trySync(it)
                    titleUpdated(id)
                }
            }
    }

    suspend fun <T> trySync(data: SourceResponse<T>) {
        transactionRunner {
            store.trySync(data)
        }
    }

    fun needUpdateTitlesWithStatus(
        status: TrackStatus?,
        expiry: Instant = ExpiryConstants.TITLES_WITH_STATUS.minutes.inPast
    ) = entityLastRequest.isRequestBefore(
        Request.MANGAS_WITH_STATUS,
        status?.priority?.toLong() ?: TrackStatus.entries.size.toLong(),
        expiry
    )

    fun needUpdateTitle(
        id: Long,
        expiry: Instant = ExpiryConstants.TITLE_DETAILS.minutes.inPast
    ) = entityLastRequest.isRequestBefore(
        Request.MANGA_DETAILS,
        id,
        expiry
    )

    fun needUpdateTitleRoles(
        id: Long,
        expiry: Instant = ExpiryConstants.TITLE_ROLES.minutes.inPast
    ) = entityLastRequest.isRequestBefore(
        Request.MANGA_ROLES,
        id,
        expiry
    )

    fun titleUpdated(id: Long) = entityLastRequest.updateLastRequest(Request.MANGA_DETAILS, id)
    fun rolesUpdated(id: Long) = entityLastRequest.updateLastRequest(Request.MANGA_ROLES, id)
    fun statusUpdated(status: TrackStatus?) = entityLastRequest.updateLastRequest(
        Request.MANGAS_WITH_STATUS,
        status?.priority?.toLong() ?: TrackStatus.entries.size.toLong()
    )
}
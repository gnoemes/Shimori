package com.gnoemes.shimori.data.ranobe

import com.gnoemes.shimori.base.extensions.inPast
import com.gnoemes.shimori.data.app.ExpiryConstants
import com.gnoemes.shimori.data.app.Request
import com.gnoemes.shimori.data.app.SourceResponse
import com.gnoemes.shimori.data.db.api.db.DatabaseTransactionRunner
import com.gnoemes.shimori.data.lastrequest.EntityLastRequestStore
import com.gnoemes.shimori.data.source.catalogue.CatalogueManager
import com.gnoemes.shimori.data.titles.manga.MangaInfo
import com.gnoemes.shimori.data.track.ListSort
import com.gnoemes.shimori.data.track.TrackStatus
import com.gnoemes.shimori.data.user.UserShort
import com.gnoemes.shimori.logging.api.Logger
import kotlinx.datetime.Instant
import me.tatarka.inject.annotations.Inject
import kotlin.time.Duration.Companion.minutes

@Inject
class RanobeRepository(
    private val catalogue: CatalogueManager,
    private val store: SyncedRanobeStore,
    private val entityLastRequest: EntityLastRequestStore,
    private val logger: Logger,
    private val transactionRunner: DatabaseTransactionRunner
) {

    fun queryById(id: Long) = store.dao.queryById(id)
    fun observeById(id: Long) = store.dao.observeById(id)
    fun paging(status: TrackStatus, sort: ListSort) = store.dao.paging(status, sort)

    suspend fun syncTracked(
        user: UserShort,
        status: TrackStatus?
    ): SourceResponse<List<MangaInfo>> {
        return catalogue.ranobe { getWithStatus(user, status) }
            .also {
                transactionRunner {
                    store.trySync(it)
                    statusUpdated(status)
                }
            }
    }

    suspend fun sync(id: Long): SourceResponse<MangaInfo> {
        val local = store.dao.queryById(id)
            ?: throw IllegalStateException("Ranobe with id: $id not found")

        return catalogue.ranobe { get(local) }
            .also {
                transactionRunner {
                    store.trySync(it)
                    titleUpdated(id)
                }
            }
    }

    suspend fun syncTitleCharacters(
        id: Long
    ): SourceResponse<MangaInfo> {
        val local = store.dao.queryById(id)
            ?: throw IllegalStateException("Ranobe with id: $id not found")

        return catalogue.ranobe { getCharacters(local) }
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
        Request.RANOBE_WITH_STATUS,
        status?.priority?.toLong() ?: TrackStatus.entries.size.toLong(),
        expiry
    )

    fun needUpdateTitle(
        id: Long,
        expiry: Instant = ExpiryConstants.TITLE_DETAILS.minutes.inPast
    ) = entityLastRequest.isRequestBefore(
        Request.RANOBE_DETAILS,
        id,
        expiry
    )

    fun titleUpdated(id: Long) = entityLastRequest.updateLastRequest(Request.RANOBE_DETAILS, id)
    fun statusUpdated(status: TrackStatus?) = entityLastRequest.updateLastRequest(
        Request.RANOBE_WITH_STATUS,
        status?.priority?.toLong() ?: TrackStatus.entries.size.toLong()
    )
}
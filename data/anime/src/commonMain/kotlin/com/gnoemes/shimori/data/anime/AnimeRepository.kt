package com.gnoemes.shimori.data.anime

import com.gnoemes.shimori.base.extensions.inPast
import com.gnoemes.shimori.data.app.ExpiryConstants
import com.gnoemes.shimori.data.app.Request
import com.gnoemes.shimori.data.app.SourceResponse
import com.gnoemes.shimori.data.db.api.db.DatabaseTransactionRunner
import com.gnoemes.shimori.data.lastrequest.EntityLastRequestStore
import com.gnoemes.shimori.data.source.catalogue.CatalogueManager
import com.gnoemes.shimori.data.titles.anime.AnimeInfo
import com.gnoemes.shimori.data.track.ListSort
import com.gnoemes.shimori.data.track.TrackStatus
import com.gnoemes.shimori.data.user.UserShort
import com.gnoemes.shimori.logging.api.Logger
import kotlinx.datetime.Instant
import me.tatarka.inject.annotations.Inject
import kotlin.time.Duration.Companion.minutes

@Inject
class AnimeRepository(
    private val catalogue: CatalogueManager,
    private val store: SyncedAnimeStore,
    private val videoStore: AnimeVideoStore,
    private val screenshotStore: AnimeScreenshotStore,
    private val entityLastRequest: EntityLastRequestStore,
    private val logger: Logger,
    private val transactionRunner: DatabaseTransactionRunner
) {

    fun queryById(id: Long) = store.dao.queryById(id)
    fun observeById(id: Long) = store.dao.observeById(id)
    fun observeVideos(id: Long) = videoStore.dao.observeByTitleId(id)
    fun observeScreenshots(id: Long) = screenshotStore.dao.observeByTitleId(id)
    fun observeScreenshotsCount(id: Long) = screenshotStore.dao.observeCountByTitleId(id)
    fun paging(status: TrackStatus, sort: ListSort) = store.dao.paging(status, sort)
    fun observeStatusExists(status: TrackStatus) = store.dao.observeStatusExists(status)

    suspend fun syncTracked(
        user: UserShort,
        status: TrackStatus?
    ): SourceResponse<List<AnimeInfo>> {
        return catalogue.anime { getWithStatus(user, status) }
            .also {
                transactionRunner {
                    store.trySync(it)
                    statusUpdated(status)
                }
            }
    }

    suspend fun sync(id: Long): SourceResponse<AnimeInfo> {
        val local = store.dao.queryById(id)
            ?: throw IllegalStateException("Anime with id: $id not found")

        return catalogue.anime { get(local) }
            .also {
                transactionRunner {
                    store.trySync(it)
                    videoStore.trySync(it)
                    screenshotStore.trySync(it)
                    titleUpdated(id)
                }
            }
    }

    suspend fun syncTitleCharacters(
        id: Long
    ) : SourceResponse<AnimeInfo> {
        val local = store.dao.queryById(id)
            ?: throw IllegalStateException("Anime with id: $id not found")

        return catalogue.anime { getCharacters(local) }
    }

    suspend fun syncTitlePersons(
        id: Long
    ) : SourceResponse<AnimeInfo> {
        val local = store.dao.queryById(id)
            ?: throw IllegalStateException("Anime with id: $id not found")

        return catalogue.anime { getPersons(local) }
    }

    suspend fun <T> trySync(data: SourceResponse<T>) {
        transactionRunner {
            store.trySync(data)
            videoStore.trySync(data)
            screenshotStore.trySync(data)
        }
    }

    fun needUpdateTitlesWithStatus(
        status: TrackStatus?,
        expiry: Instant = ExpiryConstants.TITLES_WITH_STATUS.minutes.inPast
    ) = entityLastRequest.isRequestBefore(
        Request.ANIMES_WITH_STATUS,
        status?.priority?.toLong() ?: TrackStatus.entries.size.toLong(),
        expiry
    )

    fun needUpdateTitle(
        id: Long,
        expiry: Instant = ExpiryConstants.TITLE_DETAILS.minutes.inPast
    ) = entityLastRequest.isRequestBefore(
        Request.ANIME_DETAILS,
        id,
        expiry
    )

    fun titleUpdated(id: Long) = entityLastRequest.updateLastRequest(Request.ANIME_DETAILS, id)
    fun statusUpdated(status: TrackStatus?) = entityLastRequest.updateLastRequest(
        Request.ANIMES_WITH_STATUS,
        status?.priority?.toLong() ?: TrackStatus.entries.size.toLong()
    )
}
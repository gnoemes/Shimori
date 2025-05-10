package com.gnoemes.shimori.data.anime

import com.gnoemes.shimori.data.app.Request
import com.gnoemes.shimori.data.app.SourceResponse
import com.gnoemes.shimori.data.core.BaseCatalogueRepository
import com.gnoemes.shimori.data.db.api.db.DatabaseTransactionRunner
import com.gnoemes.shimori.data.lastrequest.EntityLastRequestStore
import com.gnoemes.shimori.data.source.catalogue.CatalogueManager
import com.gnoemes.shimori.data.titles.anime.Anime
import com.gnoemes.shimori.data.track.ListSort
import com.gnoemes.shimori.data.track.TrackStatus
import com.gnoemes.shimori.data.user.UserShort
import com.gnoemes.shimori.logging.api.Logger
import com.gnoemes.shimori.source.model.SourceDataType
import me.tatarka.inject.annotations.Inject

@Inject
class AnimeRepository(
    logger: Logger,
    catalogue: CatalogueManager,
    entityLastRequest: EntityLastRequestStore,
    private val store: SyncedAnimeStore,
    private val videoStore: AnimeVideoStore,
    private val screenshotStore: AnimeScreenshotStore,
    private val transactionRunner: DatabaseTransactionRunner
) : BaseCatalogueRepository<Anime>(
    SourceDataType.Anime,
    logger,
    catalogue,
    entityLastRequest,
    transactionRunner
) {
    override fun queryById(id: Long) = store.dao.queryById(id)
    fun observeById(id: Long) = store.dao.observeById(id)
    fun observeVideos(id: Long) = videoStore.dao.observeByTitleId(id)
    fun observeScreenshots(id: Long) = screenshotStore.dao.observeByTitleId(id)
    fun observeScreenshotsCount(id: Long) = screenshotStore.dao.observeCountByTitleId(id)
    fun paging(status: TrackStatus, sort: ListSort) = store.dao.paging(status, sort)
    fun observeStatusExists(status: TrackStatus) = store.dao.observeStatusExists(status)

    suspend fun syncTracked(
        user: UserShort,
        status: TrackStatus?
    ) = request {
        anime { getWithStatus(user, status) }
    }.also {
        transactionRunner {
            store.trySync(it)
            statusUpdated(status)
        }
    }

    suspend fun sync(id: Long) =
        request(id) {
            anime { get(it) }
        }.also {
            transactionRunner {
                store.trySync(it)
                videoStore.trySync(it)
                screenshotStore.trySync(it)
                titleUpdated(id)
            }
        }

    suspend fun syncTitleCharacters(
        id: Long
    ) = request(id) {
        anime { getCharacters(it) }
    }

    suspend fun syncTitlePersons(
        id: Long
    ) = request(id) {
        anime { getPersons(it) }
    }

    suspend fun syncTitleRelated(
        id: Long
    ) = request(id) {
        anime { getRelated(it) }
    }

    override fun <T> trySyncTransaction(data: SourceResponse<T>) {
        store.trySync(data)
        videoStore.trySync(data)
        screenshotStore.trySync(data)
    }

    fun shouldUpdateTitlesWithStatus(
        status: TrackStatus?,
    ) = shouldUpdate(
        Request.ANIMES_WITH_STATUS,
        status?.priority?.toLong() ?: TrackStatus.entries.size.toLong(),
    )

    fun shouldUpdateTitle(
        id: Long,
    ) = shouldUpdate(
        Request.ANIME_DETAILS,
        id,
    )

    fun shouldUpdateTitleRelated(
        id: Long,
    ) = shouldUpdate(
        Request.ANIME_DETAILS_RELATED,
        id,
    )

    private fun titleUpdated(id: Long) = updated(Request.ANIME_DETAILS, id)
    fun titleRelatedUpdated(id: Long) = updated(Request.ANIME_DETAILS_RELATED, id)
    private fun statusUpdated(status: TrackStatus?) = updated(
        Request.ANIMES_WITH_STATUS,
        status?.priority?.toLong() ?: TrackStatus.entries.size.toLong()
    )
}
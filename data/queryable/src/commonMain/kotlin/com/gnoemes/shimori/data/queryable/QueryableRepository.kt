package com.gnoemes.shimori.data.queryable

import com.gnoemes.shimori.data.db.api.daos.MangaAndRanobeQueryableDao
import com.gnoemes.shimori.data.db.api.daos.RelatedDao
import com.gnoemes.shimori.data.db.api.db.DatabaseTransactionRunner
import com.gnoemes.shimori.data.track.ListSort
import com.gnoemes.shimori.data.track.TrackStatus
import com.gnoemes.shimori.data.track.TrackTargetType
import com.gnoemes.shimori.logging.api.Logger
import me.tatarka.inject.annotations.Inject

/**
 * Contains query logic on several entities at once
 */
@Inject
class QueryableRepository(
    private val mangaAndRanobeQueryableDao: MangaAndRanobeQueryableDao,
    private val relatedDao : RelatedDao,
    private val logger: Logger,
    private val transactionRunner: DatabaseTransactionRunner
) {
    fun observeStatusExists(status: TrackStatus) = mangaAndRanobeQueryableDao.observeStatusExists(status)
    fun pagingMangaAndRanobe(status: TrackStatus, sort: ListSort) = mangaAndRanobeQueryableDao.paging(status, sort)

    fun pagingRelated(
        targetId: Long,
        targetType: TrackTargetType,
    ) = relatedDao.paging(targetId, targetType)
}
package com.gnoemes.shimori.data.repositories.ranobe

import androidx.paging.PagingSource
import com.gnoemes.shimori.data.daos.RanobeDao
import com.gnoemes.shimori.data.sync.syncerForEntity
import com.gnoemes.shimori.data.util.DatabaseTransactionRunner
import com.gnoemes.shimori.model.ranobe.Ranobe
import com.gnoemes.shimori.model.ranobe.RanobeWithRate
import com.gnoemes.shimori.model.rate.RateSort
import com.gnoemes.shimori.model.rate.RateSortOption
import com.gnoemes.shimori.model.rate.RateStatus
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RanobeStore @Inject constructor(
    private val runner: DatabaseTransactionRunner,
    private val ranobeDao: RanobeDao,
) {
    private val syncer = syncerForEntity(
        ranobeDao,
        { it.shikimoriId },
        { remote, local -> remote.copy(id = local?.id ?: 0) }
    )

    suspend fun update(ranobe: List<Ranobe>) = runner {
        syncer.sync(ranobeDao.queryAll(), ranobe, removeNotMatched = false)
    }

    suspend fun queryRandomByStatus(status: RateStatus?) =
        if (status == null) ranobeDao.queryRandomPinned()
        else ranobeDao.queryRandomByStatus(status)

    fun observeByStatusForPaging(
        status: RateStatus,
        sort: RateSort
    ): PagingSource<Int, RanobeWithRate> {
        return when (sort.sortOption) {
            RateSortOption.NAME -> {
                //TODO restore romadzi or make cross language search
//                if (!settings.isRomadziNaming) ranobeDao.pagingNameRu(status, sort.isDescending)
//                else
                ranobeDao.pagingName(status, sort.isDescending)
            }
            RateSortOption.PROGRESS -> ranobeDao.pagingProgress(status, sort.isDescending)
            RateSortOption.DATE_CREATED -> ranobeDao.pagingDateCreated(status, sort.isDescending)
            RateSortOption.DATE_UPDATED -> ranobeDao.pagingDateUpdated(status, sort.isDescending)
            RateSortOption.DATE_AIRED -> ranobeDao.pagingDateAired(status, sort.isDescending)
            RateSortOption.MY_SCORE -> ranobeDao.pagingScore(status, sort.isDescending)
            RateSortOption.SIZE -> ranobeDao.pagingSize(status, sort.isDescending)
            RateSortOption.RATING -> ranobeDao.pagingRating(status, sort.isDescending)
            else -> throw IllegalArgumentException("$sort sort is not supported")
        }
    }

    fun observePinned(sort: RateSort): Flow<List<RanobeWithRate>> {
        return when (sort.sortOption) {
            RateSortOption.PROGRESS -> ranobeDao.pinnedProgress(sort.isDescending)
            RateSortOption.DATE_CREATED -> ranobeDao.pinnedDateCreated(sort.isDescending)
            RateSortOption.DATE_UPDATED -> ranobeDao.pinnedDateUpdated(sort.isDescending)
            RateSortOption.DATE_AIRED -> ranobeDao.pinnedDateAired(sort.isDescending)
            RateSortOption.MY_SCORE -> ranobeDao.pinnedScore(sort.isDescending)
            RateSortOption.RATING -> ranobeDao.pinnedRating(sort.isDescending)
            RateSortOption.NAME -> {
                //TODO paging eng, restore romadzi or make cross results
//                if (!settings.isRomadziNaming) animeDao.pagingNameRu(status, sort.isDescending)
//                else
                ranobeDao.pinnedName(sort.isDescending)
            }
            else -> throw IllegalArgumentException("$sort sort is not supported")
        }
    }
    
    fun observeById(id: Long) = ranobeDao.observeById(id)
}
package com.gnoemes.shimori.data.repositories.ranobe

import androidx.paging.PagingSource
import com.gnoemes.shimori.base.settings.ShimoriPreferences
import com.gnoemes.shimori.data.daos.RanobeDao
import com.gnoemes.shimori.data.sync.syncerForEntity
import com.gnoemes.shimori.data.util.DatabaseTransactionRunner
import com.gnoemes.shimori.model.ranobe.Ranobe
import com.gnoemes.shimori.model.ranobe.RanobeWithRate
import com.gnoemes.shimori.model.rate.RateSort
import com.gnoemes.shimori.model.rate.RateSortOption
import com.gnoemes.shimori.model.rate.RateStatus
import javax.inject.Inject

class RanobeStore @Inject constructor(
    private val runner: DatabaseTransactionRunner,
    private val ranobeDao: RanobeDao,
    private val settings: ShimoriPreferences
) {
    private val syncer = syncerForEntity(
            ranobeDao,
            { it.shikimoriId },
            { entity, id -> entity.copy(id = id ?: 0) }
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
                if (!settings.isRomadziNaming) ranobeDao.pagingNameRu(status, sort.isDescending)
                else ranobeDao.pagingName(status, sort.isDescending)
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

}
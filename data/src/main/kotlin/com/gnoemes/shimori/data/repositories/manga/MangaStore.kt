package com.gnoemes.shimori.data.repositories.manga

import androidx.paging.PagingSource
import com.gnoemes.shimori.base.settings.ShimoriPreferences
import com.gnoemes.shimori.data.daos.MangaDao
import com.gnoemes.shimori.data.sync.syncerForEntity
import com.gnoemes.shimori.data.util.DatabaseTransactionRunner
import com.gnoemes.shimori.model.manga.Manga
import com.gnoemes.shimori.model.manga.MangaWithRate
import com.gnoemes.shimori.model.rate.RateSort
import com.gnoemes.shimori.model.rate.RateSortOption
import com.gnoemes.shimori.model.rate.RateStatus
import javax.inject.Inject

class MangaStore @Inject constructor(
    private val runner: DatabaseTransactionRunner,
    private val mangaDao: MangaDao,
    private val settings: ShimoriPreferences
) {
    private val syncer = syncerForEntity(
            mangaDao,
            { it.shikimoriId },
            { remote, local -> remote.copy(id = local?.id ?: 0) }
    )

    suspend fun update(mangas : List<Manga>) = runner {
        syncer.sync(mangaDao.queryAll(), mangas, removeNotMatched = false)
    }

    suspend fun queryRandomByStatus(status: RateStatus?) =
        if (status == null) mangaDao.queryRandomPinned()
        else mangaDao.queryRandomByStatus(status)

    fun observeByStatusForPaging(
        status: RateStatus,
        sort: RateSort
    ) : PagingSource<Int, MangaWithRate> {
        return when(sort.sortOption) {
            RateSortOption.NAME -> {
                if (!settings.isRomadziNaming) mangaDao.pagingNameRu(status, sort.isDescending)
                else mangaDao.pagingName(status, sort.isDescending)
            }
            RateSortOption.PROGRESS -> mangaDao.pagingProgress(status, sort.isDescending)
            RateSortOption.DATE_CREATED -> mangaDao.pagingDateCreated(status, sort.isDescending)
            RateSortOption.DATE_UPDATED -> mangaDao.pagingDateUpdated(status, sort.isDescending)
            RateSortOption.DATE_AIRED -> mangaDao.pagingDateAired(status, sort.isDescending)
            RateSortOption.MY_SCORE -> mangaDao.pagingScore(status, sort.isDescending)
            RateSortOption.SIZE -> mangaDao.pagingSize(status, sort.isDescending)
            RateSortOption.RATING -> mangaDao.pagingRating(status, sort.isDescending)
            else -> throw IllegalArgumentException("$sort sort is not supported")
        }
    }

}
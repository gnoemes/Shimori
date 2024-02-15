package com.gnoemes.shimori.data.lists

import com.gnoemes.shimori.data.db.api.daos.ListPinDao
import com.gnoemes.shimori.data.db.api.daos.ListSortDao
import com.gnoemes.shimori.data.db.api.db.DatabaseTransactionRunner
import com.gnoemes.shimori.data.track.ListSort
import com.gnoemes.shimori.data.track.ListType
import com.gnoemes.shimori.data.track.TrackTargetType
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class ListsRepository(
    private val dao: ListPinDao,
    private val listSortDao: ListSortDao,
    private val transactionRunner: DatabaseTransactionRunner,
) {
    fun paging(
        sort: ListSort
    ) = dao.paging(sort)

    fun observeListSort(type: ListType): Flow<ListSort?> = listSortDao.observe(type)
    fun observePinExist(targetId: Long, targetType: TrackTargetType) =
        dao.observePinExist(targetId, targetType)

    fun observePinsExist() = dao.observePinsExist()

    fun togglePin(targetId: Long, targetType: TrackTargetType) = transactionRunner {
        dao.togglePin(targetId, targetType)
    }


    fun pin(targetId: Long, targetType: TrackTargetType, pin: Boolean) = transactionRunner {
        dao.pin(targetId, targetType, pin)
    }
}


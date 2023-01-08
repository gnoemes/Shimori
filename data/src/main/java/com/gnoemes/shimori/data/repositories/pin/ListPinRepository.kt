package com.gnoemes.shimori.data.repositories.pin

import com.gnoemes.shimori.data.core.database.daos.ListPinDao
import com.gnoemes.shimori.data.core.entities.track.ListSort
import com.gnoemes.shimori.data.core.entities.track.TrackTargetType


class ListPinRepository constructor(
    private val dao: ListPinDao
) {

    fun paging(
        sort: ListSort
    ) = dao.paging(sort)

    fun observePinExist(targetId: Long, targetType: TrackTargetType) =
        dao.observePinExist(targetId, targetType)

    fun observePinsExist() = dao.observePinsExist()

    suspend fun togglePin(targetId: Long, targetType: TrackTargetType) =
        dao.togglePin(targetId, targetType)

    suspend fun pin(targetId: Long, targetType: TrackTargetType, pin: Boolean) =
        dao.pin(targetId, targetType, pin)
}
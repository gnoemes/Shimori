package com.gnoemes.shimori.data.repositories.pin

import com.gnoemes.shimori.data.core.database.daos.ListPinDao
import com.gnoemes.shimori.data.core.entities.rate.RateTargetType


class ListPinRepository constructor(
    private val dao: ListPinDao
) {

    fun observePinExist(targetId: Long, targetType: RateTargetType) =
        dao.observePinExist(targetId, targetType)

    fun observePinsExist() = dao.observePinsExist()

    suspend fun togglePin(targetId: Long, targetType: RateTargetType) =
        dao.togglePin(targetId, targetType)

    suspend fun pin(targetId: Long, targetType: RateTargetType, pin: Boolean) =
        dao.pin(targetId, targetType, pin)
}
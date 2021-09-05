package com.gnoemes.shimori.data.repositories.pin

import com.gnoemes.shimori.data.daos.ListPinDao
import com.gnoemes.shimori.model.app.ListPin
import com.gnoemes.shimori.model.rate.RateTargetType
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ListPinStore @Inject constructor(
    private val dao: ListPinDao
) {

    fun observeHasPin(
        targetId: Long,
        targetType: RateTargetType
    ) = dao.observeByTarget(targetId, targetType).map { it > 0 }

    fun observeHasPins() = dao.observeSize().map { it > 0 }

    suspend fun pin(type: RateTargetType, id: Long, pin : Boolean) {
        val local = dao.queryByTarget(id, type)
        if (pin) {
            if (local != null) return
            dao.insert(ListPin(targetId = id, targetType = type))
        } else {
            if (local == null) return
            dao.delete(local)
        }
    }

    suspend fun togglePin(type: RateTargetType, id: Long) {
        val local = dao.queryByTarget(id, type)
        if (local != null) {
            dao.delete(local)
        } else {
            dao.insert(ListPin(targetId = id, targetType = type))
        }
    }
}
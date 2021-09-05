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

    suspend fun pin(type: RateTargetType, shikimoriId: Long, pin : Boolean) {
        val local = dao.queryById(shikimoriId)
        if (pin) {
            if (local != null) return
            dao.insert(ListPin(targetId = shikimoriId, targetType = type))
        } else {
            if (local == null) return
            dao.delete(local)
        }
    }

    suspend fun togglePin(type: RateTargetType, shikimoriId: Long) {
        val local = dao.queryById(shikimoriId)
        if (local != null) {
            dao.delete(local)
        } else {
            dao.insert(ListPin(targetId = shikimoriId, targetType = type))
        }
    }
}
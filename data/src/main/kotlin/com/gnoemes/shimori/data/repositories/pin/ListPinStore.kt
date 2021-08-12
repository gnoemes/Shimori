package com.gnoemes.shimori.data.repositories.pin

import com.gnoemes.shimori.data.daos.ListPinDao
import com.gnoemes.shimori.model.app.ListPin
import com.gnoemes.shimori.model.rate.RateTargetType
import javax.inject.Inject

class ListPinStore @Inject constructor(
    private val dao: ListPinDao
) {

    suspend fun togglePin(type: RateTargetType, shikimoriId: Long) {
        val local = dao.queryById(shikimoriId)
        if (local != null) {
            dao.delete(local)
        } else {
            dao.insert(ListPin(targetId = shikimoriId, targetType = type))
        }
    }
}
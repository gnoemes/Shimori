package com.gnoemes.shimori.data.repositories.pin

import com.gnoemes.shimori.data.daos.ListPinDao
import com.gnoemes.shimori.model.rate.RateTargetType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ListPinStore @Inject constructor(
    private val dao: ListPinDao
) {

    fun observeHasPinPage(type: RateTargetType): Flow<Boolean> {
        return dao.observePinnedSize(type).map { it > 0 }
    }

    suspend fun pin(id: Long, type: RateTargetType) {
        //TODO
    }
}
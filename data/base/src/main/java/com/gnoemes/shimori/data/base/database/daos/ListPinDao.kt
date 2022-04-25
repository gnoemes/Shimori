package com.gnoemes.shimori.data.base.database.daos

import com.gnoemes.shimori.data.base.entities.app.ListPin
import com.gnoemes.shimori.data.base.entities.rate.RateTargetType
import kotlinx.coroutines.flow.Flow

abstract class ListPinDao : EntityDao<ListPin>() {
    abstract suspend fun pin(targetId: Long, targetType: RateTargetType, pin: Boolean): Boolean
    abstract suspend fun togglePin(targetId: Long, targetType: RateTargetType): Boolean

    abstract fun observePinExist(targetId: Long, targetType: RateTargetType): Flow<Boolean>
    abstract fun observePinsExist(): Flow<Boolean>
}
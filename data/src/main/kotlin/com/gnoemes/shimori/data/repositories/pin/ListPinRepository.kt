package com.gnoemes.shimori.data.repositories.pin

import com.gnoemes.shimori.model.rate.RateTargetType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ListPinRepository @Inject constructor(
    private val store: ListPinStore
) {

    fun observeHasPin(targetId: Long, targetType: RateTargetType) = store.observeHasPin(targetId, targetType)
    fun observeHasPins() = store.observeHasPins()

    suspend fun togglePin(type : RateTargetType, id : Long) = store.togglePin(type, id)
    suspend fun pin(type: RateTargetType, id: Long, pin: Boolean) = store.pin(type, id, pin)
}
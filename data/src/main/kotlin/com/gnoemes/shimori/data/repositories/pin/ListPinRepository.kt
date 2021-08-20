package com.gnoemes.shimori.data.repositories.pin

import com.gnoemes.shimori.model.rate.RateTargetType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ListPinRepository @Inject constructor(
    private val store : ListPinStore
) {

    fun observeHasPins() = store.observeHasPins()

    suspend fun togglePin(type : RateTargetType, shikimoriId : Long) = store.togglePin(type, shikimoriId)
}
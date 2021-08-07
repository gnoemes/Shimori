package com.gnoemes.shimori.lists

import com.gnoemes.shimori.base.settings.ShimoriPreferences
import com.gnoemes.shimori.model.rate.RateTargetType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ListsStateManager @Inject constructor(
    prefs: ShimoriPreferences
) {

    private val _currentType =
        MutableStateFlow(RateTargetType.findOrDefault(prefs.preferredRateType))

    val currentType: StateFlow<RateTargetType> get() = _currentType

    private val _updatingRates = MutableStateFlow(false)

    val updatingRates: StateFlow<Boolean> = _updatingRates

    fun updateType(type: RateTargetType) {
        _currentType.value = type
    }

    fun updatingRates(updating: Boolean) {
        _updatingRates.value = updating
    }


}
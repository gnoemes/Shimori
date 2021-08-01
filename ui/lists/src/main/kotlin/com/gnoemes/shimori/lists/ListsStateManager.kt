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

    fun updateType(type: RateTargetType) {
        _currentType.value = type
    }

    val currentType: StateFlow<RateTargetType> get() = _currentType
}
package com.gnoemes.shimori.lists

import com.gnoemes.shimori.base.settings.ShimoriPreferences
import com.gnoemes.shimori.model.rate.RateStatus
import com.gnoemes.shimori.model.rate.RateTargetType
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
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
    val updatingRates: StateFlow<Boolean> get() = _updatingRates

    private val _currentPage = MutableStateFlow(RateStatus.WATCHING)
    val currentPage: StateFlow<RateStatus> get() = _currentPage

    private val _openRandomTitle = MutableSharedFlow<Unit>()
    val openRandomTitle: SharedFlow<Unit> get() = _openRandomTitle

    fun updateType(type: RateTargetType) {
        _currentType.value = type
    }

    fun updatingRates(updating: Boolean) {
        _updatingRates.value = updating
    }

    fun updateCurrentPage(status: RateStatus) {
        _currentPage.value = status
    }

    suspend fun openRandomTitle() {
        _openRandomTitle.emit(Unit)
    }

}
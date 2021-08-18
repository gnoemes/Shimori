package com.gnoemes.shimori.lists

import com.gnoemes.shimori.base.settings.ShimoriPreferences
import com.gnoemes.shimori.model.rate.ListType
import com.gnoemes.shimori.model.rate.RateStatus
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

    /**
     * Represents selected [ListType]
     */
    val type = object : State<ListType> {
        private val currentType = MutableStateFlow(ListType.findOrDefault(prefs.preferredListType))
        override fun update(newState: ListType) =
            kotlin.run { currentType.value = newState }

        override val observe: StateFlow<ListType> get() = currentType
    }

    /**
     * Represents current active list page by [RateStatus]
     */
    val page = object : State<RateStatus> {
        private val currentPage = MutableStateFlow(RateStatus.WATCHING)
        override fun update(newState: RateStatus) =
            kotlin.run { currentPage.value = newState }

        override val observe: StateFlow<RateStatus> get() = currentPage
    }

    /**
     * Sync loading across screens
     */
    val ratesLoading = object : State<Boolean> {
        private val updatingRates = MutableStateFlow(false)
        override fun update(newState: Boolean) =
            kotlin.run { updatingRates.value = newState }

        override val observe: StateFlow<Boolean> get() = updatingRates
    }

    /**
     * Event to notify listeners to open random title from current [RateStatus] or [ListType.Pinned]
     */
    val openRandomTitleEvent = object : EventState<Unit> {
        private val openRandomTitle = MutableSharedFlow<Unit>()
        override suspend fun update(newState: Unit) =
            kotlin.run { openRandomTitle.emit(newState) }

        override val observe: SharedFlow<Unit> get() = openRandomTitle
    }

    /**
     * Interface for common used state features
     */
    interface State<T> {
        fun update(newState: T)
        val observe: StateFlow<T>
        val value: T get() = observe.value

        operator fun invoke(newState: T) = update(newState = newState)
    }

    /**
     * Interface for trigger events
     */
    interface EventState<T> {
        suspend fun update(newState: T)
        val observe: SharedFlow<T>

        suspend operator fun invoke(newState: T) = update(newState = newState)
    }
}
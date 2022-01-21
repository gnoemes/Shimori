package com.gnoemes.shimori.data.repositories.rates

import com.gnoemes.shimori.base.settings.ShimoriSettings
import com.gnoemes.shimori.model.rate.ListType
import com.gnoemes.shimori.model.rate.RateStatus
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ListsStateManager @Inject constructor(
    settings: ShimoriSettings,

    ) {

    /**
     * Represents selected [ListType]
     */
    val type = object : State<ListType> {
        override suspend fun update(newState: ListType) {
            settings.preferredListType(newState.type)
        }

        override val observe: Flow<ListType>
            get() = settings.preferredListType.observe.map { ListType.findOrDefault(it) }
    }

    /**
     * Represents current active list page by [RateStatus]
     */
    val page = object : State<RateStatus> {
        override suspend fun update(newState: RateStatus) {
            settings.preferredListStatus(newState.shikimoriValue)
        }

        override val observe: Flow<RateStatus>
            get() = settings.preferredListStatus.observe.map { RateStatus.find(it)!! }
    }

    /**
     * Sync loading status across screens
     */
    val ratesLoading = object : State<Boolean> {
        private val ratesLoading = MutableStateFlow(false)
        override suspend fun update(newState: Boolean) =
            kotlin.run { ratesLoading.value = newState }

        override val observe: StateFlow<Boolean> get() = ratesLoading
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
        suspend fun update(newState: T)
        val observe: Flow<T>

        suspend operator fun invoke(newState: T) = update(newState = newState)
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
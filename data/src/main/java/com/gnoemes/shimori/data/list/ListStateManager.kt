package com.gnoemes.shimori.data.list

import com.gnoemes.shimori.base.core.settings.ShimoriSettings
import com.gnoemes.shimori.base.core.settings.invoke
import com.gnoemes.shimori.data.core.entities.track.ListType
import com.gnoemes.shimori.data.core.entities.track.TrackStatus
import kotlinx.coroutines.flow.*

class ListsStateBus constructor(
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
     * Represents current active list page by [TrackStatus]
     */
    val page = object : State<TrackStatus> {
        override suspend fun update(newState: TrackStatus) {
            settings.preferredListStatus(newState.name)
        }

        override val observe: Flow<TrackStatus>
            get() = settings.preferredListStatus.observe.map { TrackStatus.valueOf(it) }
    }

    /**
     * Sync loading status across screens
     */
    val tracksLoading = object : State<Boolean> {
        private val tracksLoading = MutableStateFlow(false)
        override suspend fun update(newState: Boolean) =
            kotlin.run { tracksLoading.value = newState }

        override val observe: StateFlow<Boolean> get() = tracksLoading
    }

    /**
     * Event to notify listeners to open random title from current [TrackStatus] or [ListType.Pinned]
     */
    val openRandomTitleEvent = object : EventState<Unit> {
        private val openRandomTitle = MutableSharedFlow<Unit>()
        override suspend fun update(newState: Unit) =
            kotlin.run { openRandomTitle.emit(newState) }

        override val observe: SharedFlow<Unit> get() = openRandomTitle
    }

    val uiEvents = object : EventState<ListsUiEvents> {
        private val uiEvents = MutableSharedFlow<ListsUiEvents>()
        override suspend fun update(newState: ListsUiEvents) =
            kotlin.run { uiEvents.emit(newState) }

        override val observe: SharedFlow<ListsUiEvents> = uiEvents
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
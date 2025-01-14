package com.gnoemes.shimori.data.lists

import com.gnoemes.shimori.data.track.ListType
import com.gnoemes.shimori.data.track.TrackStatus
import com.gnoemes.shimori.preferences.ShimoriPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppScope::class)
class ListsStateBus(
    prefs: ShimoriPreferences,
) {

    /**
     * Represents selected [ListType]
     */
    val type = object : State<ListType> {
        override suspend fun update(newState: ListType) {
            prefs.preferredListType = newState.type
        }

        override val observe: Flow<ListType>
            get() = prefs.observePreferredListType().map { ListType.findOrDefault(it) }
    }

    /**
     * Represents current active list page by [TrackStatus]
     */
    val page = object : State<TrackStatus> {
        override suspend fun update(newState: TrackStatus) {
            prefs.preferredListStatus = newState.name
        }

        override val observe: Flow<TrackStatus>
            get() = prefs.observePreferredListStatus().map { TrackStatus.valueOf(it) }
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
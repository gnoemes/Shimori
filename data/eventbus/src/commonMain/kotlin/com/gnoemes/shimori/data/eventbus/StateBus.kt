package com.gnoemes.shimori.data.eventbus

import com.gnoemes.shimori.data.track.TrackStatus
import com.gnoemes.shimori.data.track.TrackTargetType
import com.gnoemes.shimori.preferences.ShimoriPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppScope::class)
class StateBus(
    prefs: ShimoriPreferences,
) {

    /**
     * Represents selected [TrackTargetType]
     */
    val type = object : State<TrackTargetType> {
        override suspend fun update(newState: TrackTargetType) {
            prefs.preferredListType = newState.name
        }

        override val observe: Flow<TrackTargetType> =
            prefs.observePreferredListType().map { TrackTargetType.valueOf(it) }
    }

    /**
     * Represents current active list page by [TrackStatus]
     */
    val page = object : State<TrackStatus> {
        override suspend fun update(newState: TrackStatus) {
            prefs.preferredListStatus = newState.name
        }

        override val observe: Flow<TrackStatus> =
            prefs.observePreferredListStatus().map { TrackStatus.valueOf(it) }
    }

    /**
     * Sync loading status across screens
     */
    val tracksLoading = object : State<Boolean> {
        private val tracksLoading = MutableStateFlow(false)
        override suspend fun update(newState: Boolean) =
            kotlin.run { tracksLoading.value = newState }

        override val observe: StateFlow<Boolean> = tracksLoading
    }

    /**
     * Sync loading status across screens
     */
    val catalogueSyncActive = object : State<Boolean> {
        private val catalogueSync = MutableStateFlow(false)
        override suspend fun update(newState: Boolean) =
            kotlin.run { catalogueSync.value = newState }

        override val observe: StateFlow<Boolean> = catalogueSync
    }

    /**
     * Interface for common used state features
     */
    interface State<T> {
        suspend fun update(newState: T)
        val observe: Flow<T>

        suspend operator fun invoke(newState: T) = update(newState = newState)
    }

}
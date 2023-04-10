package com.gnoemes.shimori.common.ui.navigation

import cafe.adriel.voyager.core.registry.ScreenProvider
import com.gnoemes.shimori.data.core.entities.track.TrackTargetType

sealed class FeatureScreen : ScreenProvider {
    object Home : FeatureScreen()
    object Lists : FeatureScreen()
    object ListMenu : FeatureScreen()
    data class TrackEdit(
        val id: Long,
        val type: TrackTargetType,
        val markComplete: Boolean,
        val deleteNotification: Boolean
    ) : FeatureScreen()

    data class TitleDetails(val id: Long, val type: TrackTargetType) : FeatureScreen()

    object Settings : FeatureScreen()
    object Auth : FeatureScreen()
}

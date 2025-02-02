package com.gnoemes.shimori.screens

import com.gnoemes.shimori.data.track.TrackStatus
import com.gnoemes.shimori.data.track.TrackTargetType
import com.slack.circuit.runtime.screen.Screen

@Parcelize
object HomeScreen : ShimoriScreen("Home()")

@Parcelize
object TracksScreen : ShimoriScreen("Tracks()")
@Parcelize
object TracksEmptyScreen : ShimoriScreen("TracksEmpty()")

@Parcelize
data class ExploreScreen(
    val type: TrackTargetType? = null,
) : ShimoriScreen("Explore()") {
    override val arguments get() = mapOf("type" to type)
}

@Parcelize
object TracksMenuScreen : ShimoriScreen("TracksMenu()")

@Parcelize
object AuthScreen : ShimoriScreen("Auth()")

@Parcelize
object SettingsScreen : ShimoriScreen("Settings()"), OverlayScreen

@Parcelize
data class SettingsAppearanceScreen(val card: Boolean = false) :
    ShimoriScreen("SettingsAppearance()") {
    override val arguments get() = mapOf("card" to card)
}

@Parcelize
data class EditTrackScreen(
    val targetId: Long,
    val targetType: TrackTargetType,
    val predefinedStatus: TrackStatus?
) : ShimoriScreen("EditTrack()"), OverlayScreen {
    override val arguments get() = mapOf(
        "targetId" to targetId,
        "targetType" to targetType,
        "predefinedStatus" to predefinedStatus,
    )
}

@Parcelize
object MockScreen : ShimoriScreen("Mock()")

@Parcelize
data class UrlScreen(val url: String) : ShimoriScreen(name = "UrlScreen()") {
    override val arguments get() = mapOf("url" to url)
}

abstract class ShimoriScreen(val name: String) : Screen {
    open val arguments: Map<String, *>? = null
}

sealed interface OverlayScreen

enum class OverlayType {
    None,
    Dialog,
    BottomSheet,
    SideSheet
}

expect fun overlayTypeFor(screen: OverlayScreen): OverlayType
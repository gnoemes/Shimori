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
object SearchScreen : ShimoriScreen("Search()")

@Parcelize
object SettingsScreen : ShimoriScreen("Settings()")

@Parcelize
data class SettingsAppearanceScreen(val card: Boolean = false) :
    ShimoriScreen("SettingsAppearance()") {
    override val arguments get() = mapOf("card" to card)
}

@Parcelize
data class TrackEditScreen(
    val targetId: Long,
    val targetType: TrackTargetType,
    val predefinedStatus: TrackStatus?
) : ShimoriScreen("TrackEdit()") {
    override val arguments
        get() = mapOf(
            "targetId" to targetId,
            "targetType" to targetType,
            "predefinedStatus" to predefinedStatus,
        )
}


@Parcelize
data class TitleDetailsScreen(
    val id: Long,
    val type: TrackTargetType
) : ShimoriScreen("TitleDetails()")


@Parcelize
data class TitleCharactersScreen(
    val id: Long,
    val type: TrackTargetType,
    val asContent: Boolean,
) : ShimoriScreen("TitleCharacters()")

@Parcelize
data class TitleTrailersScreen(
    val id: Long,
    val type: TrackTargetType,
    val asContent: Boolean,
) : ShimoriScreen("TitleTrailers()")

@Parcelize
data class TitleFramesScreen(
    val id: Long,
) : ShimoriScreen("TitleFrames()")

@Parcelize
data class TitleRelatedScreen(
    val id: Long,
    val type: TrackTargetType,
    val asContent: Boolean,
) : ShimoriScreen("TitleRelated()")

@Parcelize
data class TitleSimilarScreen(
    val id: Long,
    val type: TrackTargetType,
    val asContent: Boolean,
) : ShimoriScreen("TitleSimilar()")

@Parcelize
data class CharacterDetailsScreen(
    val id: Long,
) : ShimoriScreen("CharacterDetails()")

@Parcelize
data class PersonDetailsScreen(
    val id: Long,
) : ShimoriScreen("PersonDetails()")

@Parcelize
object MockScreen : ShimoriScreen("Mock()")

@Parcelize
data class UrlScreen(val url: String) : ShimoriScreen(name = "UrlScreen()") {
    override val arguments get() = mapOf("url" to url)
}

abstract class ShimoriScreen(val name: String) : Screen {
    open val arguments: Map<String, *>? = null
}
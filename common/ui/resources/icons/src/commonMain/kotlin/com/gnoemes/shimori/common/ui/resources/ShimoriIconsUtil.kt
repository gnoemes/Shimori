package com.gnoemes.shimori.common.ui.resources

import com.gnoemes.shimori.base.inject.UiScope
import com.gnoemes.shimori.common.ui.resources.icons.ic_anime
import com.gnoemes.shimori.common.ui.resources.icons.ic_completed
import com.gnoemes.shimori.common.ui.resources.icons.ic_dropped
import com.gnoemes.shimori.common.ui.resources.icons.ic_in_progress
import com.gnoemes.shimori.common.ui.resources.icons.ic_manga
import com.gnoemes.shimori.common.ui.resources.icons.ic_on_hold
import com.gnoemes.shimori.common.ui.resources.icons.ic_planned
import com.gnoemes.shimori.common.ui.resources.icons.ic_ranobe
import com.gnoemes.shimori.common.ui.resources.icons.ic_re_do
import com.gnoemes.shimori.common.ui.resources.icons.ic_shikimori
import com.gnoemes.shimori.data.track.TrackStatus
import com.gnoemes.shimori.data.track.TrackTargetType
import com.gnoemes.shimori.source.Source
import com.gnoemes.shimori.sources.SourceIds
import me.tatarka.inject.annotations.Inject
import org.jetbrains.compose.resources.DrawableResource
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn


@SingleIn(UiScope::class)
@Inject
class ShimoriIconsUtil {

    fun sourceIcon(source: Source): DrawableResource? = sourceIcon(source.id)

    fun sourceIcon(id: Long): DrawableResource? = when (id) {
        SourceIds.SHIKIMORI -> Icons.ic_shikimori
        else -> null
    }

    fun icon(type: TrackTargetType): DrawableResource = when (type) {
        TrackTargetType.ANIME -> Icons.ic_anime
        TrackTargetType.MANGA -> Icons.ic_manga
        TrackTargetType.RANOBE -> Icons.ic_ranobe
    }

    fun trackStatusIcon(status: TrackStatus): DrawableResource = when (status) {
        TrackStatus.PLANNED -> Icons.ic_planned
        TrackStatus.WATCHING -> Icons.ic_in_progress
        TrackStatus.REWATCHING -> Icons.ic_re_do
        TrackStatus.COMPLETED -> Icons.ic_completed
        TrackStatus.ON_HOLD -> Icons.ic_on_hold
        TrackStatus.DROPPED -> Icons.ic_dropped
    }
}
package com.gnoemes.shimori.common.ui.resources

import com.gnoemes.shimori.base.inject.UiScope
import com.gnoemes.shimori.common.ui.resources.icons.ic_anime
import com.gnoemes.shimori.common.ui.resources.icons.ic_completed
import com.gnoemes.shimori.common.ui.resources.icons.ic_dropped
import com.gnoemes.shimori.common.ui.resources.icons.ic_in_progress
import com.gnoemes.shimori.common.ui.resources.icons.ic_manga
import com.gnoemes.shimori.common.ui.resources.icons.ic_on_hold
import com.gnoemes.shimori.common.ui.resources.icons.ic_pin
import com.gnoemes.shimori.common.ui.resources.icons.ic_planned
import com.gnoemes.shimori.common.ui.resources.icons.ic_ranobe
import com.gnoemes.shimori.common.ui.resources.icons.ic_re_do
import com.gnoemes.shimori.data.track.ListType
import com.gnoemes.shimori.data.track.TrackStatus
import me.tatarka.inject.annotations.Inject
import org.jetbrains.compose.resources.DrawableResource
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn


@SingleIn(UiScope::class)
@Inject
class ShimoriIconsUtil {
    fun listIcon(type: ListType): DrawableResource = when (type) {
        ListType.Pinned -> Icons.ic_pin
        ListType.Anime -> Icons.ic_anime
        ListType.Manga -> Icons.ic_manga
        else -> Icons.ic_ranobe
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
package com.gnoemes.shimori.common.ui.resources

import com.gnoemes.shimori.base.inject.ActivityScope
import com.gnoemes.shimori.common.ui.resources.icons.Res
import com.gnoemes.shimori.data.track.ListType
import com.gnoemes.shimori.data.track.TrackStatus
import dev.icerock.moko.resources.ImageResource
import me.tatarka.inject.annotations.Inject


@ActivityScope
@Inject
class ShimoriIconsUtil {
    fun listIcon(type: ListType): ImageResource = when (type) {
        ListType.Pinned -> Res.images.ic_pin
        ListType.Anime -> Res.images.ic_anime
        ListType.Manga -> Res.images.ic_manga
        else -> Res.images.ic_ranobe
    }

    fun trackStatusIcon(status: TrackStatus): ImageResource = when (status) {
        TrackStatus.PLANNED -> Res.images.ic_planned
        TrackStatus.WATCHING -> Res.images.ic_in_progress
        TrackStatus.REWATCHING -> Res.images.ic_re_do
        TrackStatus.COMPLETED -> Res.images.ic_completed
        TrackStatus.ON_HOLD -> Res.images.ic_on_hold
        TrackStatus.DROPPED -> Res.images.ic_dropped
    }
}
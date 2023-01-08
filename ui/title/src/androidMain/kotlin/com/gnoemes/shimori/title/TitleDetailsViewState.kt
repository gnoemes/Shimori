package com.gnoemes.shimori.title

import androidx.compose.runtime.Immutable
import com.gnoemes.shimori.data.core.entities.TitleWithTrackEntity

@Immutable
internal data class TitleDetailsViewState(
    val title: TitleWithTrackEntity? = null,
) {
}
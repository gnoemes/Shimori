package com.gnoemes.shimori.title

import androidx.compose.runtime.Immutable
import com.gnoemes.shimori.data.core.entities.TitleWithRateEntity

@Immutable
internal data class TitleDetailsViewState(
    val title: TitleWithRateEntity? = null,
) {
}
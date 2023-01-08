package com.gnoemes.shimori.main

import androidx.compose.runtime.Immutable
import com.gnoemes.shimori.base.core.settings.AppLocale
import com.gnoemes.shimori.base.core.settings.AppTitlesLocale
import com.gnoemes.shimori.data.core.entities.track.ListType

@Immutable
data class MainViewState(
    val listType: ListType = ListType.Anime
) {

    companion object {
        val Empty = MainViewState()
    }
}

@Immutable
data class MainSettingsViewState(
    val titlesLocale: AppTitlesLocale,
    val appLocale: AppLocale,
)
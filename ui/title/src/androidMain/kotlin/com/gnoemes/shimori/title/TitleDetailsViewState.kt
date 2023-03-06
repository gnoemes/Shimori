package com.gnoemes.shimori.title

import androidx.compose.runtime.Immutable
import com.gnoemes.shimori.common.ui.api.OptionalContent
import com.gnoemes.shimori.data.core.entities.TitleWithTrackEntity
import com.gnoemes.shimori.data.core.entities.characters.Character
import com.gnoemes.shimori.data.core.entities.titles.anime.AnimeScreenshot
import com.gnoemes.shimori.data.core.entities.titles.anime.AnimeVideo

@Immutable
internal data class TitleDetailsViewState(
    val title: TitleWithTrackEntity? = null,
    val characters: OptionalContent<List<Character>?> = OptionalContent(false, null),
    val videos: OptionalContent<List<AnimeVideo>?> = OptionalContent(false, null),
    val screenshots: OptionalContent<List<AnimeScreenshot>?> = OptionalContent(false, null),
)
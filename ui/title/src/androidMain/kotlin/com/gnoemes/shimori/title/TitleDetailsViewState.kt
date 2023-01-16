package com.gnoemes.shimori.title

import androidx.compose.runtime.Immutable
import com.gnoemes.shimori.data.core.entities.TitleWithTrackEntity
import com.gnoemes.shimori.data.core.entities.characters.Character

@Immutable
internal data class TitleDetailsViewState(
    val title: TitleWithTrackEntity? = null,
    val characters: List<Character>? = null,
)
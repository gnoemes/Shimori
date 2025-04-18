package com.gnoemes.shimori.title.trailers

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.gnoemes.shimori.base.inject.UiScope
import com.gnoemes.shimori.common.compose.NestedScaffold
import com.gnoemes.shimori.common.compose.placeholder
import com.gnoemes.shimori.common.compose.ui.TrailerItem
import com.gnoemes.shimori.common.compose.ui.TransparentToolbar
import com.gnoemes.shimori.common.ui.resources.Icons
import com.gnoemes.shimori.common.ui.resources.icons.ic_back
import com.gnoemes.shimori.data.titles.anime.AnimeVideo
import com.gnoemes.shimori.screens.TitleTrailersScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import org.jetbrains.compose.resources.painterResource

@Composable
@CircuitInject(screen = TitleTrailersScreen::class, scope = UiScope::class)
internal fun TitleTrailersUi(
    state: TitleTrailersUiState,
    modifier: Modifier = Modifier,
) {
    val eventSink = state.eventSink

    TitleTrailersUi(
        asContent = state.asContent,
        titleName = state.titleName,
        trailers = state.trailers,
        navigateUp = { eventSink(TitleTrailersUiEvent.NavigateUp) },
        openTrailer = { eventSink(TitleTrailersUiEvent.OpenTrailer(it)) },
    )
}

@Composable
private fun TitleTrailersUi(
    asContent: Boolean,
    titleName: String,
    trailers: List<AnimeVideo>?,
    navigateUp: () -> Unit,
    openTrailer: (Long) -> Unit,
) {
    if (asContent) {
        TitleTrailersUiContent(trailers, openTrailer)
    } else {
        TitleTrailersUiScreenContent(
            titleName, trailers, navigateUp, openTrailer
        )
    }
}


@Composable
private fun TitleTrailersUiContent(
    trailers: List<AnimeVideo>?,
    openTrailer: (Long) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(Modifier.width(0.dp))

        if (trailers == null) {
            repeat(3) {
                Box(
                    modifier = Modifier.width(280.dp)
                        .height(156.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .placeholder(visible = true),
                )
            }
        } else {
            trailers.forEach {
                TrailerItem(it, onClick = { openTrailer(it.id) })
            }
        }

        Spacer(Modifier.width(0.dp))
    }
}

@Composable
private fun TitleTrailersUiScreenContent(
    titleName: String,
    trailers: List<AnimeVideo>?,
    navigateUp: () -> Unit,
    openTrailer: (Long) -> Unit,
) {
    NestedScaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TransparentToolbar(
                navigationIcon = {
                    IconButton(navigateUp) {
                        Icon(painterResource(Icons.ic_back), contentDescription = null)
                    }
                },
                onNavigationClick = navigateUp,
                title = {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            titleName,
                            modifier = Modifier.fillMaxWidth(),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                        Text(
                            titleName,
                            modifier = Modifier.fillMaxWidth(),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                },
            )
        }
    ) { paddingValue ->

    }
}


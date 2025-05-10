package com.gnoemes.shimori.title.related

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.itemKey
import com.gnoemes.shimori.base.inject.UiScope
import com.gnoemes.shimori.common.compose.placeholder
import com.gnoemes.shimori.common.compose.rememberLazyListState
import com.gnoemes.shimori.common.compose.ui.RelatedItem
import com.gnoemes.shimori.data.common.Related
import com.gnoemes.shimori.data.track.TrackTargetType
import com.gnoemes.shimori.screens.TitleRelatedScreen
import com.slack.circuit.codegen.annotations.CircuitInject

@Composable
@CircuitInject(screen = TitleRelatedScreen::class, scope = UiScope::class)
internal fun TitleRelatedUi(
    state: TitleRelatedUiState,
    modifier: Modifier = Modifier,
) {
    val eventSink = state.eventSink

    TitleCharactersUi(
        isList = state.isList,
        related = state.related,
        navigateUp = { eventSink(TitleRelatedUiEvent.NavigateUp) },
        openTitle = { id, type -> eventSink(TitleRelatedUiEvent.OpenTitle(id, type)) },
        openEdit = { id, type -> eventSink(TitleRelatedUiEvent.OpenEdit(id, type)) },
    )
}

@Composable
private fun TitleCharactersUi(
    isList: Boolean,
    related: LazyPagingItems<Related>,
    navigateUp: () -> Unit,
    openTitle: (Long, TrackTargetType) -> Unit,
    openEdit: (Long, TrackTargetType) -> Unit,
) {
    if (isList) {
        TitleRelatedUiListContent(
            related,
            openTitle,
            openEdit,
        )
    } else {
        //TODO tab view
    }
}


@Composable
private fun TitleRelatedUiListContent(
    relatedItems: LazyPagingItems<Related>,
    openTitle: (Long, TrackTargetType) -> Unit,
    openEdit: (Long, TrackTargetType) -> Unit,
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        state = relatedItems.rememberLazyListState(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { Spacer(Modifier.width(0.dp)) }

        if (relatedItems.itemCount == 0) {
            repeat(10) {
                item {
                    Box(
                        modifier = Modifier.width(132.dp)
                            .aspectRatio(3 / 4f)
                            .clip(MaterialTheme.shapes.medium)
                            .placeholder(true),
                    )
                }
            }
        }

        items(
            count = relatedItems.itemCount,
            key = relatedItems.itemKey { "related_${it.id}_${it.targetType}" },
        ) { index ->
            val related = relatedItems[index]
            if (related != null) {
                val openRelatedTitle = remember(related.relatedId) {
                    { openTitle(related.title.id, related.title.type) }
                }
                val openRelatedEdit = remember(related.relatedId) {
                    { openEdit(related.title.id, related.title.type) }
                }


                RelatedItem(
                    related = related,
                    onClick = openRelatedTitle,
                    openEdit = openRelatedEdit
                )
            }
        }

        item { Spacer(Modifier.width(0.dp)) }
    }
}

